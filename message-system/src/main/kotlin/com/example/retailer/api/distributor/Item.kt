package com.example.retailer.api.distributor

import javax.persistence.*

/**
 * Описание товара
 */

@Entity
@Table(name = "items")
data class Item(
    /**
     * Произвольный идентификатор
     */
    @Id
    val id: Long,

    /**
     * Произвольное название
     */
    @Column
    val name: String
)