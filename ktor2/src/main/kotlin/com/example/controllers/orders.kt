package com.example.controllers

import com.example.models.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

fun Route.orders() {
    route("/orders/{id}") {
        get("/get") {
            val orders = transaction { Order.find { Orders.u_id eq call.parameters["id"]!! } }
            if (transaction { orders.count() } == 0L) {
                call.respond("[]")
            } else {
                val mList: MutableList<OrderJson> = mutableListOf()
                transaction {
                    orders.forEach { order ->
                        mList.add(OrderJson(order))
                    }
                }
                call.respond(mList)
            }
        }
        post("/post") {
            val cardOrder = call.receive<CardOrder>()
            val orderJson = cardOrder.order
            val cardDetailsJson = cardOrder.cardDetails
            if (cardDetailsJson.validate()) {
                transaction {
                    val basket = Basket.find(Baskets._id eq orderJson.basketJson._id).firstOrNull()
                    if (basket == null) {
                        Basket.new {
                            _id = orderJson.basketJson._id
                            u_id = "orders"
                        }
                    } else {
                        basket.u_id = "orders"
                        val basketProducts = BasketProduct.find(BasketProducts.b_id eq basket._id)
                        basketProducts.forEach { it.delete() }
                    }
                    Order.new {
                        _id = orderJson._id
                        u_id = call.parameters["id"]!!
                        b_id = orderJson.basketJson._id
                    }
                    orderJson.basketJson.products.forEach {
                        BasketProduct.new {
                            this._id = it._id
                            this.p_id = it.p_id
                            this.b_id = orderJson.basketJson._id
                            this.quantity = it.quantity
                        }
                    }
                }
                call.respondText("Successfully added order.")
            }
            else {
                call.respondText("Could not validate credit card.")
            }
        }
    }
}

fun Application.ordersRoutes() {
    routing {
        authenticate("auth") {
            orders()
        }
    }
}