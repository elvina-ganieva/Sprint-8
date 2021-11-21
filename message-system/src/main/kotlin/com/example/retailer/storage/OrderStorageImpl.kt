package com.example.retailer.storage

import com.example.retailer.api.distributor.Order
import com.example.retailer.api.distributor.OrderInfo
import com.example.retailer.api.distributor.OrderStatus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component("orderStorage")
class OrderStorageImpl : OrderStorage {

    @Autowired
    lateinit var orderRepo: OrderRepo

    @Autowired
    lateinit var orderInfoRepo: OrderInfoRepo

    override fun createOrder(draftOrder: Order): PlaceOrderData {
        val order = orderRepo.save(draftOrder)
        val orderInfo = orderInfoRepo.save(
            OrderInfo(
                order.id!!,
                OrderStatus.SENT,
                "signature"
            )
        )
        return PlaceOrderData(order, orderInfo)
    }

    override fun updateOrder(order: OrderInfo): Boolean {
        if (orderRepo.existsById(order.orderId.toLong())) {
            orderInfoRepo.save(order)
            return true
        } else
            return false
    }

    override fun getOrderInfo(id: String): OrderInfo? {
        return orderInfoRepo.findByIdOrNull(id.toLong())
    }
}
