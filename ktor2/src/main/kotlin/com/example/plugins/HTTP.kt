package com.example.plugins

import io.ktor.http.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*

fun Application.configureHTTP() {
    install(CORS) {
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Put)
        allowHost("localhost:8080", subDomains = listOf("sub1"))
        allowHost("google.com")
    }

}
