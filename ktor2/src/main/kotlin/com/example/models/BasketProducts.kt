package com.example.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object BasketProducts : IntIdTable() {
    val _id = varchar("_id", 48)
    val p_id = varchar("p_id", 48).references(Products._id)
    val b_id = varchar("b_id", 48).references(Baskets._id)
    val quantity = integer("quantity")
}

class BasketProduct(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<BasketProduct>(BasketProducts)

    var _id by BasketProducts._id
    var p_id by BasketProducts.p_id
    var b_id by BasketProducts.b_id
    var quantity by BasketProducts.quantity
}

@Serializable
data class BasketProductJson(var _id: String, var p_id: String, var quantity: Int)