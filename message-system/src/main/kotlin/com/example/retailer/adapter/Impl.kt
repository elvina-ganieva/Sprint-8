package com.example.retailer.adapter

import com.example.retailer.api.distributor.Order
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.amqp.core.Message
import org.springframework.amqp.core.MessagePostProcessor
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component("distributorPublisher")
class Impl : DistributorPublisher {
    @Autowired
    private lateinit var template: RabbitTemplate

    @Autowired
    private lateinit var topic: TopicExchange

    override fun placeOrder(order: Order): Boolean {
        val objectMapper = ObjectMapper()
        val message = objectMapper.writeValueAsString(order)
        println("message: $message")
        template.convertAndSend(message, topic.name) { m: Message ->
            m.messageProperties.headers["Notify-Exchange"] = "distributor_exchange"
            m.messageProperties.headers["Notify-RoutingKey"] = "retailer.myGitHub.#"
            m
        }
        return true
    }
}