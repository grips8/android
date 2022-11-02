package com.example.controllers

import com.example.models.Category
import com.example.models.CategoryJson
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.transactions.transaction

fun Route.categories() {
    get("/categories") {
        val categories = transaction { Category.all() }
        val categoriesJson: MutableList<CategoryJson> = arrayListOf()
        transaction {
            categories.forEach {
                categoriesJson.add(CategoryJson(it))
            }
        }
        call.respond(categoriesJson)
    }

    post("/addcategory") {
        val categoryJson = call.receive<CategoryJson>()
        transaction {
            Category.new {
                name = categoryJson.name
                tax = categoryJson.tax
                exclusiveness = categoryJson.exclusiveness
            }
        }
        call.respondText("Successfully added the product. =)")
    }
}

fun Application.categoriesRoutes() {
    routing {
        categories()
    }
}