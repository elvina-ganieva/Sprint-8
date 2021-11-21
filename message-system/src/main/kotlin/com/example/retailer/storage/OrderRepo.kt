package com.example.retailer.storage

import com.example.retailer.api.distributor.Order
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderRepo : CrudRepository<Order, Long>
