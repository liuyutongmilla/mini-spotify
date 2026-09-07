package com.example.routes

import com.example.repository.MusicRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

/** Registers the REST endpoints consumed by the Android client. */
fun Route.musicRoutes(repository: MusicRepository) {

    get("/") {
        call.respond(mapOf("service" to "spotify-backend", "status" to "ok"))
    }

    get("/health") {
        call.respond(mapOf("status" to "UP"))
    }

    get("/feed") {
        call.respond(repository.getHomeFeed())
    }

    get("/playlists") {
        call.respond(repository.getPlaylists())
    }

    get("/playlist/{id}") {
        val id = call.parameters["id"]?.toIntOrNull()
        if (id == null || id <= 0) {
            call.respond(HttpStatusCode.BadRequest, mapOf("message" to "Invalid playlist id"))
            return@get
        }

        val playlist = repository.getPlaylist(id)
        if (playlist == null) {
            call.respond(HttpStatusCode.NotFound, mapOf("message" to "Playlist not found"))
        } else {
            call.respond(playlist)
        }
    }
}
