package com.example.routes

import io.ktor.server.http.content.resources
import io.ktor.server.http.content.static
import io.ktor.server.routing.Route

/** Serves the bundled audio files under `/songs/*`. */
fun Route.staticMediaRoutes() {
    static("/") {
        staticBasePackage = "static"
        static("songs") {
            resources("songs")
        }
    }
}
