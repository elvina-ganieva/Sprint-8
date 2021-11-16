package ru.sberschool.hystrix

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import feign.Feign
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

    private val client = HystrixFeign.builder()
        .client(ApacheHttpClient())
        .decoder(JacksonDecoder(mapper))
        .options(Request.Options(1, TimeUnit.SECONDS, 1, TimeUnit.SECONDS, true))
        .target(SlowlyApi::class.java, "https://pokeapi.co/api/v2/", FallbackSlowlyApi())

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
    fun `getFirstPokemon should return no name`() {
        // given
        MockServerClient("127.0.0.1", 18080)
            .`when`(
                // задаем матчер для нашего запроса
                HttpRequest.request()
                    .withMethod("GET")
                    .withPath("/pokemon/?limit=1")
            )
            .respond(
                HttpResponse.response()
                    .withStatusCode(400)
                    .withDelay(TimeUnit.SECONDS, 30)
            )
        // expect
        assertEquals("no name", client.getFirstPokemon().results!![0].name)
//        println("pokemon name: ${client.getFirstPokemon().results!![0].name}")
    }

    @Test
    fun `getFirstAbility should return stench`() {
        // given
        MockServerClient("127.0.0.1", 18080)
            .`when`(
                // задаем матчер для нашего запроса
                HttpRequest.request()
                    .withMethod("GET")
                    .withPath("/ability/?limit=1")
            )
            .respond(
                HttpResponse.response()
                    .withStatusCode(201)
            )
        // expect
        assertEquals("stench", client.getFirstAbility().results!![0].name)
//        println("ability name: ${client.getFirstAbility().results!![0].name}")
    }


    @Test
    fun `getFirstLocation should return canalave-city`() {
        // given
        MockServerClient("127.0.0.1", 18080)
            .`when`(
                // задаем матчер для нашего запроса
                HttpRequest.request()
                    .withMethod("GET")
                    .withPath("/location/?limit=1")
            )
            .respond(
                HttpResponse.response()
                    .withStatusCode(201)
            )
        // expect
        assertEquals("canalave-city", client.getFirstLocation().results!![0].name)
//        println("location name: ${client.getFirstLocation().results!![0].name}")
    }

    @Test
    fun `getFirstGame should return generation-i`() {
        // given
        MockServerClient("127.0.0.1", 18080)
            .`when`(
                // задаем матчер для нашего запроса
                HttpRequest.request()
                    .withMethod("GET")
                    .withPath("/generation/?limit=1")
            )
            .respond(
                HttpResponse.response()
                    .withStatusCode(201)
            )
        // expect
        assertEquals("generation-i", client.getFirstGame().results!![0].name)
//        println("game name: ${client.getFirstGame().results!![0].name}")
    }
}
