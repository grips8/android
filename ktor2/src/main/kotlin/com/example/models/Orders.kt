package com.example.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.transactions.transaction

object Orders : IntIdTable() {
    val _id = varchar("_id", 48)
    val u_id = varchar("u_id", 48)
    val b_id = varchar("b_id", 48).references(Baskets._id)
}

class Order(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Order>(Orders)

    var _id by Orders._id
    var u_id by Orders.u_id
    var b_id by Orders.b_id
}

@Serializable
data class OrderJson(var _id: String, @SerialName("basket") var basketJson: BasketJson) {
    constructor(order: Order) : this(order.b_id, BasketJson("placeholder", listOf())) {
        val basket = transaction { Basket.find { Baskets._id eq order.b_id }.first() }
        basketJson = BasketJson(basket)
    }
}

@Serializable
data class CardDetailsJson(val name: String, val number: String, val exp: String, val cvv: String) {
    fun validate() : Boolean {      // mock validation
        return true
    }
}

@Serializable
data class CardOrder(var order: OrderJson, var cardDetails: CardDetailsJson)