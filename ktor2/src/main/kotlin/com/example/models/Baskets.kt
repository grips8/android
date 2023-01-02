package com.example.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Baskets : IntIdTable() {
    val _id = varchar("_id", 48)
    val u_id = varchar("u_id", 48)
}

class Basket(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Basket>(Baskets)

    var _id by Baskets._id
    var u_id by Baskets.u_id
}

@Serializable
data class BasketJson(var _id: String, val products: List<BasketProductJson>)