package ru.sberschool.hystrix

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import feign.Request
import feign.httpclient.ApacheHttpClient
import feign.hystrix.HystrixFeign
import feign.jackson.JacksonDecoder
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockserver.client.server.MockServerClient
import org.mockserver.integration.ClientAndServer
import org.mockserver.model.HttpRequest
import org.mockserver.model.HttpResponse
import java.util.concurrent.TimeUnit


class SlowlyApiTest {

    private val mapper = ObjectMapper()
        .registerModule(KotlinModule())
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    private val mockClient = HystrixFeign.builder()
        .client(ApacheHttpClient())
        .decoder(JacksonDecoder(mapper))
        .options(Request.Options(1, TimeUnit.SECONDS, 1, TimeUnit.SECONDS, true))
        .target(SlowlyApi::class.java, "http://127.0.0.1:18080", FallbackSlowlyApi())

    private val pokeApiClient = HystrixFeign.builder()
        .client(ApacheHttpClient())
        .decoder(JacksonDecoder(mapper))
        .options(Request.Options(1, TimeUnit.SECONDS, 1, TimeUnit.SECONDS, true))
        .target(SlowlyApi::class.java, "https://pokeapi.co/api/v2", FallbackSlowlyApi())

    lateinit var mockServer: ClientAndServer

    @BeforeEach
    fun setup() {
        // запускаем мок сервер для тестирования клиента
        mockServer = ClientAndServer.startClientAndServer(18080)
    }

    @AfterEach
    fun shutdown() {
        mockServer.stop()
    }

    @Test
    fun `getFourthAbility should return fallback from mock client`() {
        MockServerClient("127.0.0.1", 18080)
            .`when`(
                // задаем матчер для нашего запроса
                HttpRequest.request()
                    .withMethod("GET")
                    .withPath("/ability/4")
            )
            .respond(
                HttpResponse.response()
                    .withStatusCode(400)
                    .withDelay(TimeUnit.SECONDS, 30)
                    .withBody("{\"name\": \"battle-armor\"}")
            )
        assertEquals("fallback", mockClient.getFourthAbility().name)
    }

    @Test
    fun `getFourthAbility should return battle-armor from mock server`() {
        // given
        MockServerClient("127.0.0.1", 18080)
            .`when`(
                HttpRequest.request()
                    .withMethod("GET")
                    .withPath("/ability/4")
            )
            .respond(
                HttpResponse.response()
                    .withStatusCode(200)
                    .withBody("{\"name\": \"battle-armor\"}")
            )
        assertEquals("battle-armor", mockClient.getFourthAbility().name)
    }

    @Test
    fun `getFourthAbility1 should return battle-armor from actual api`() {
        assertEquals("battle-armor", pokeApiClient.getFourthAbility().name)
    }

    @Test
    fun `getFourthAbility2 should return battle-armor from actual api`() {
        assertEquals("battle-armor", pokeApiClient.getFourthAbility().name)
    }
}
