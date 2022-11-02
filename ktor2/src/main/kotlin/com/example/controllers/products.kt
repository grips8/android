package com.example.controllers

import com.example.models.Product
import com.example.models.ProductJson
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.transactions.transaction

fun Route.products() {
    get("/products") {
        val products = transaction { Product.all() }
        val productsJson: MutableList<ProductJson> = arrayListOf()
        transaction {
            products.forEach {
                productsJson.add(ProductJson(it))
            }
        }
        call.respond(productsJson)
    }

    post("/addproduct") {
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

fun Application.customerRoutes() {
    routing {
        products()
    }
}