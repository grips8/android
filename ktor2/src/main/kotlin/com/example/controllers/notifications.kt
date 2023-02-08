package com.example.controllers

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.notifications() {
    route("notifications") {
        post("/say-hi") {
            call.respond("Hello from the server side! :)")
        }

    }
}

fun Application.notificationsRoutes() {
    routing {
        notifications()
    }
}
