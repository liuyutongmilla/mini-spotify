package com.example

import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.http.content.resources
import io.ktor.server.http.content.static
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

@Serializable
data class Album(
    val id: Int,
    val album: String,
    val year: String,
    val cover: String,
    val artists: String,
    val description: String
)

@Serializable
data class Section(
    @SerialName("section_title")
    val sectionTitle: String,
    val albums: List<Album>
)

@Serializable
data class Song(
    val name: String,
    val lyric: String,
    val src: String,
    val length: String
)

@Serializable
data class Playlist(
    val id: Int,
    val songs: List<Song>
)

/** Starts the Ktor server on port 8080. */
fun main() {
    embeddedServer(
        Netty,
        port = 8080,
        host = "0.0.0.0",
        module = Application::module
    ).start(wait = true)
}

/** Configures JSON serialization, API routes, and static media delivery. */
fun Application.module() {
    val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }

    install(ContentNegotiation) {
        json(json)
    }

    routing {
        get("/") {
            call.respondText("Music Streaming Platform backend is running.")
        }

        get("/feed") {
            val feed = readResource("feed.json")
            if (feed == null) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("message" to "Feed data is unavailable"))
            } else {
                call.respondText(feed, ContentType.Application.Json)
            }
        }

        get("/playlists") {
            val playlists = readResource("playlists.json")
            if (playlists == null) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("message" to "Playlist data is unavailable"))
            } else {
                call.respondText(playlists, ContentType.Application.Json)
            }
        }

        get("/playlist/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, mapOf("message" to "Invalid playlist id"))
                return@get
            }

            val playlistsText = readResource("playlists.json")
            if (playlistsText == null) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("message" to "Playlist data is unavailable"))
                return@get
            }

            val playlists = json.decodeFromString(
                ListSerializer(Playlist.serializer()),
                playlistsText
            )
            val playlist = playlists.firstOrNull { it.id == id }

            if (playlist == null) {
                call.respond(HttpStatusCode.NotFound, mapOf("message" to "Playlist not found"))
            } else {
                call.respond(playlist)
            }
        }

        static("/songs") {
            resources("static/songs")
        }
    }
}

/** Reads a text resource bundled with the backend application. */
private fun readResource(name: String): String? {
    return Thread.currentThread().contextClassLoader.getResource(name)?.readText()
}
