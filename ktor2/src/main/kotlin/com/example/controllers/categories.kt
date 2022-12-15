package com.example.controllers

import com.example.models.Category
import com.example.models.CategoryJson
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.transactions.transaction

fun Route.categories() {
    route("/categories") {
        get() {
            val categories = transaction { Category.all() }
            val categoriesJson: MutableList<CategoryJson> = arrayListOf()
            transaction {
                categories.forEach {
                    categoriesJson.add(CategoryJson(it))
                }
            }
            call.respond(categoriesJson)
        }
        get("/{id}") {
            val category = transaction { Category.findById(call.parameters["id"]!!.toInt())}
            if (category == null) {
                call.respondText { "Error: no category with requested ID." }
            } else {
                call.respond(CategoryJson(category))
            }
        }
        put("/update/{id}") {
            val category = transaction { Category.findById(call.parameters["id"]!!.toInt()) }
            if (category == null) {
                call.respondText("Error: no category with requested ID.")
            } else {
                val categoryJson = call.receive<CategoryJson>()
                transaction {
                    category.apply {
                        _id = categoryJson._id
                        name = categoryJson.name
                    }
                }
                call.respondText("Successfully updated the category.")
            }
        }
        delete("/delete/{id}") {
            val category = transaction { Category.findById(call.parameters["id"]!!.toInt()) }
            if (category == null) {
                call.respondText("Error: no category with requested ID.")
            } else {
                transaction {
                    category.delete()
                }
                call.respondText("Successfully deleted the category.")
            }
        }

        post("/add") {
            val categoryJson = call.receive<CategoryJson>()
            transaction {
                Category.new {
                    _id = categoryJson._id
                    name = categoryJson.name
                }
            }
            call.respondText("Successfully added the category. =)")
        }
    }

}

fun Application.categoriesRoutes() {
    routing {
        categories()
    }
}