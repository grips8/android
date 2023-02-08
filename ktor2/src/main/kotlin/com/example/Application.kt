package com.example

import com.example.controllers.*
import com.example.models.*
import io.ktor.server.application.*
import com.example.plugins.*
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseToken
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.routing.*
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    val serviceAccount = File("src/main/resources/service-account.json").inputStream()
    val options: FirebaseOptions = FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
        .build()

    FirebaseApp.initializeApp(options)

    configureHTTP()
    configureSerialization()
    configureRouting()

    install(Authentication) {
        basic("auth") {
            skipWhen { call ->
                runBlocking {
                    val token = call.request.headers["firebaseIDToken"] ?: return@runBlocking false
                    if (token == "") return@runBlocking false
                    var verified: FirebaseToken? = null
                    try {
                        verified = FirebaseAuth.getInstance().verifyIdToken(token, true)
                    }
                    catch (e: Exception) {
                        println("Exception message: ${e.message}")
                    }
                    return@runBlocking  verified != null
//                    return@runBlocking true
                }
            }
        }
    }

    Database.connect("jdbc:sqlite:./data.db", "org.sqlite.JDBC")


    transaction {
        SchemaUtils.create(Products)
        SchemaUtils.create(Categories)
        SchemaUtils.create(Baskets)
        SchemaUtils.create(BasketProducts)
        SchemaUtils.create(Orders)
    }

    categoriesRoutes()
    customerRoutes()
    basketRoutes()
    ordersRoutes()
    stripeOrdersRoutes()
    notificationsRoutes()
}