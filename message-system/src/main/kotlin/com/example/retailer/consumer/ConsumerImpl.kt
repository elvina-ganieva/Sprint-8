package com.example.retailer.consumer

import com.example.retailer.api.distributor.OrderInfo
import com.example.retailer.service.OrderService
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Autowired

class ConsumerImpl : Consumer {
    @Autowired
    lateinit var orderService: OrderService

    @RabbitListener(queues = ["#{queue.name}"])
    override fun receiveUpdate(orderInfo: String) {
        val mapper = ObjectMapper()
        val info = mapper.readValue(orderInfo, OrderInfo::class.java)
        println("hello beautiful " + info.orderId)
        orderService.updateOrderInfo(info)
    }
}