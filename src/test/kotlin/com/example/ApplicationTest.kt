package com.example

import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ApplicationTest {

    /** Verifies that the backend health endpoint responds successfully. */
    @Test
    fun rootRouteReturnsSuccess() = testApplication {
        application { module() }
        val response = client.get("/")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("Music Streaming Platform backend is running.", response.bodyAsText())
    }

    /** Verifies that the home feed endpoint returns JSON data. */
    @Test
    fun feedRouteReturnsJson() = testApplication {
        application { module() }
        val response = client.get("/feed")
        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(response.bodyAsText().contains("section_title"))
    }

    /** Verifies that the playlists endpoint returns playlist data. */
    @Test
    fun playlistsRouteReturnsJson() = testApplication {
        application { module() }
        val response = client.get("/playlists")
        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(response.bodyAsText().contains("songs"))
    }

    /** Verifies that a playlist can be retrieved by its identifier. */
    @Test
    fun playlistByIdReturnsMatchingPlaylist() = testApplication {
        application { module() }
        val response = client.get("/playlist/1")
        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(response.bodyAsText().contains("Let's Meet Now"))
    }

    /** Verifies that an unknown playlist identifier returns 404. */
    @Test
    fun unknownPlaylistReturnsNotFound() = testApplication {
        application { module() }
        val response = client.get("/playlist/999")
        assertEquals(HttpStatusCode.NotFound, response.status)
    }
}
