package com.example

import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** Smoke tests for the routes registered in routes/MusicRoutes.kt and routes/StaticRoutes.kt. */
class ApplicationTest {

    @Test
    fun rootRouteReturnsServiceMetadata() = testApplication {
        application { module() }
        val response = client.get("/")
        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(response.bodyAsText().contains("spotify-backend"))
    }

    @Test
    fun healthRouteReturnsOk() = testApplication {
        application { module() }
        val response = client.get("/health")
        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(response.bodyAsText().contains("UP"))
    }

    @Test
    fun feedRouteReturnsJson() = testApplication {
        application { module() }
        val response = client.get("/feed")
        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(response.bodyAsText().contains("section_title"))
    }

    @Test
    fun playlistsRouteReturnsJson() = testApplication {
        application { module() }
        val response = client.get("/playlists")
        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(response.bodyAsText().contains("songs"))
    }

    @Test
    fun playlistByIdReturnsMatchingPlaylist() = testApplication {
        application { module() }
        val response = client.get("/playlist/1")
        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(response.bodyAsText().contains("Let's Meet Now"))
    }

    @Test
    fun invalidPlaylistIdReturnsBadRequest() = testApplication {
        application { module() }
        val response = client.get("/playlist/not-a-number")
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun unknownPlaylistIdReturnsNotFound() = testApplication {
        application { module() }
        val response = client.get("/playlist/999")
        assertEquals(HttpStatusCode.NotFound, response.status)
    }
}
