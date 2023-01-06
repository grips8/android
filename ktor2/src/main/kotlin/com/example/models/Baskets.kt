package com.example.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

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
data class BasketJson(var _id: String, var products: List<BasketProductJson>) {
    constructor(basket: Basket) : this(basket._id, listOf()) {
        val mList: MutableList<BasketProductJson> = mutableListOf()
        transaction {
            BasketProducts.select { BasketProducts.b_id eq basket._id }.forEach {
                val basketProduct: BasketProduct = BasketProduct.wrapRow(it)
                mList.add(BasketProductJson(basketProduct._id, basketProduct.p_id, basketProduct.quantity))
            }
        }
        products = mList
    }
}