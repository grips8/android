package com.example

import com.example.controllers.customerRoutes
import com.example.models.Products
import io.ktor.server.application.*
import com.example.plugins.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    configureHTTP()
    configureSerialization()
    configureRouting()
//    install(ContentNegotiation) {
//        json()
//    }

    Database.connect("jdbc:sqlite:./data.db", "org.sqlite.JDBC")


    transaction {
        SchemaUtils.create(Products)
    }

    customerRoutes()
}