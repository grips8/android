package com.example.controllers

import com.example.models.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
//import io.ktor.server.auth.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

fun Route.baskets() {
    route("/basket/{id}") {
        get("/get") {
            val row = transaction { Baskets.select { Baskets.u_id eq call.parameters["id"]!! }.firstOrNull() }
            if (row == null) {
                call.respond("[]")
            } else {
                val basket: Basket = transaction { Basket.wrapRow(row) }
                call.respond(listOf(BasketJson(basket)))
            }
        }
        post("/post") {
            val basketJson = call.receive<BasketJson>()
            transaction {
                val basket = Basket.find(Baskets.u_id eq call.parameters["id"]!!).firstOrNull()
                if (basket == null) {
                    Basket.new {
                        _id = basketJson._id
                        u_id = call.parameters["id"]!!
                    }
                }
                else {
                    val basketProducts = BasketProduct.find(BasketProducts.b_id eq basket._id)
                    basketProducts.forEach { it.delete() }
                }
                basketJson.products.forEach {
                    BasketProduct.new {
                        this._id = it._id
                        this.p_id = it.p_id
                        this.b_id = basketJson._id
                        this.quantity = it.quantity
                    }
                }
            }
            call.respondText("Successfully added basket.")
        }
        // testing of custom headers with CORS
//        get("/test"){
//            val a =  call.request.headers["keyTest"]
//            println(a)
//            val product = transaction { Product.findById(a!!.toInt())}
//            if (product == null) {
//                call.respondText { "Error: no product with requested ID." }
//            } else {
//                call.respond(ProductJson(product))
//            }
//        }
    }
    host("sub1.localhost") {
        static("/static") {
            resources("files")
        }
    }
    static("/static2") {
        resources("files")
    }
}

fun Application.basketRoutes() {
    routing {
        authenticate("auth") {
            baskets()
        }
    }
}