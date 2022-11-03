package com.example.controllers

import com.example.models.Product
import com.example.models.ProductJson
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.transactions.transaction

fun Route.products() {
    route("/products") {
        get() {
            val products = transaction { Product.all() }
            val productsJson: MutableList<ProductJson> = arrayListOf()
            transaction {
                products.forEach {
                    productsJson.add(ProductJson(it))
                }
            }
            call.respond(productsJson)
        }
        get("/{id}") {
            val product = transaction { Product.findById(call.parameters["id"]!!.toInt())}
            if (product == null) {
                call.respondText { "Error: no product with requested ID." }
            } else {
                call.respond(ProductJson(product))
            }
        }
        put("/update/{id}") {
            val product = transaction { Product.findById(call.parameters["id"]!!.toInt()) }
            if (product == null) {
                call.respondText("Error: no product with requested ID.")
            } else {
                val productJson = call.receive<ProductJson>()
                transaction {
                    product.title = productJson.title
                    product.price = productJson.price
                    product.categoryID = productJson.categoryID
                }
                call.respondText("Successfully updated the product.")
            }
        }
        delete("/delete/{id}") {
            val product = transaction { Product.findById(call.parameters["id"]!!.toInt()) }
            if (product == null) {
                call.respondText("Error: no product with requested ID.")
            } else {
                transaction {
                    product.delete()
                }
                call.respondText("Successfully deleted the product.")
            }
        }
        post("/add") {
            val productJson = call.receive<ProductJson>()
            transaction {
                Product.new {
                    title = productJson.title
                    price = productJson.price
                    categoryID = productJson.categoryID
                }
            }
            call.respondText("Successfully added the product. =)")
        }
    }
}

fun Application.customerRoutes() {
    routing {
        products()
    }
}