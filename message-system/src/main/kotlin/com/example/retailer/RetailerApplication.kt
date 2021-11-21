package com.example.retailer

import com.example.retailer.adapter.DistributorPublisher
import com.example.retailer.adapter.DistributorPublisherImpl
import com.example.retailer.consumer.Consumer
import com.example.retailer.consumer.ConsumerImpl
import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.TopicExchange
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean


@SpringBootApplication
class RetailerApplication {

    @Bean
    fun queue(): Queue {
        return Queue("retailer_queue", false, false, true)
    }

    @Bean
    fun exchange(): TopicExchange {
        return TopicExchange("distributor_exchange", true, false)
    }

    @Bean
    fun binding(queue: Queue, exchange: TopicExchange): Binding? {
        return BindingBuilder.bind(queue).to(exchange).with("retailer.elvina-ganieva.#")
    }

    @Bean
    fun receiver(): Consumer {
        return ConsumerImpl()
    }

    @Bean
    fun sender(): DistributorPublisher {
        return DistributorPublisherImpl()
    }
}

fun main(args: Array<String>) {
    runApplication<RetailerApplication>(*args)
}
