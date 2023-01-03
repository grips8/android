package com.example.controllers

import com.example.models.Categories
import com.example.models.Category
import com.example.models.Product
import com.example.models.ProductJson
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.select
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
                    product.apply {
                        _id = productJson._id
                        name = productJson.name
                        price = productJson.price
                        description = productJson.description
                        categoryID = transaction { Category.find { Categories._id eq productJson._id }.first().id.value }   // troche dzikie
                    }
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
        post("/post") {
            val productJson = call.receive<ProductJson>()
            transaction {
                Product.new {
                    _id = productJson._id
                    name = productJson.name
                    price = productJson.price
                    description = productJson.description
                    categoryID = transaction { Category.find { Categories._id eq productJson.category!!._id }.first().id.value }   // troche dzikie
                }
            }
            call.respondText("Successfully added the product. =)")
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

fun Application.customerRoutes() {
    routing {
        authenticate("auth") {
            products()
        }
    }
}