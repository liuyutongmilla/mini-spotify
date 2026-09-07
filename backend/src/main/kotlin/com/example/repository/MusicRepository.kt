package com.example.repository

import com.example.model.Playlist
import com.example.model.Section
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

/**
 * Loads catalog data bundled with the application (`feed.json`,
 * `playlists.json`) and decodes it into typed models.
 *
 * Reading from the classpath keeps this service self-contained for now;
 * swapping in a real datastore (SQL, a document store, a CMS) only requires
 * changing this class — [MusicRoutes][com.example.routes.musicRoutes] is
 * unaware of where the data comes from.
 */
class MusicRepository {
    private val json = Json { ignoreUnknownKeys = true }

    fun getHomeFeed(): List<Section> {
        val text = readResource("feed.json") ?: return emptyList()
        return json.decodeFromString(ListSerializer(Section.serializer()), text)
    }

    fun getPlaylists(): List<Playlist> {
        val text = readResource("playlists.json") ?: return emptyList()
        return json.decodeFromString(ListSerializer(Playlist.serializer()), text)
    }

    fun getPlaylist(id: Int): Playlist? {
        return getPlaylists().firstOrNull { it.id == id }
    }

    private fun readResource(name: String): String? {
        return this::class.java.classLoader.getResource(name)?.readText()
    }
}
