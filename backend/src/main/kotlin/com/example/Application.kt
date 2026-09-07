package com.example

import com.example.config.AppConfig
import com.example.repository.MusicRepository
import com.example.routes.musicRoutes
import com.example.routes.staticMediaRoutes
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import io.ktor.server.routing.routing
import kotlinx.serialization.json.Json

fun main() {
    val config = AppConfig.fromEnvironment()
    embeddedServer(
        factory = Netty,
        host = config.host,
        port = config.port,
        module = Application::module
    ).start(wait = true)
}

/**
 * Wires up JSON serialization, request logging, centralized error handling,
 * and the application's routes. [repository] defaults to a real instance but
 * can be overridden in tests.
 */
fun Application.module(repository: MusicRepository = MusicRepository()) {
    install(CallLogging)

    install(ContentNegotiation) {
        json(
            Json {
                prettyPrint = false
                ignoreUnknownKeys = true
            }
        )
    }

    install(StatusPages) {
        exception<Throwable> { call, cause ->
            environment.log.error("Unhandled request failure", cause)
            call.respond(
                HttpStatusCode.InternalServerError,
                mapOf("message" to "Internal server error")
            )
        }
    }

    routing {
        musicRoutes(repository)
        staticMediaRoutes()
    }
}
