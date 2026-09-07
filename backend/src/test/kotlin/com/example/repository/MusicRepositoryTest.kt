package com.example.repository

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MusicRepositoryTest {

    private val repository = MusicRepository()

    @Test
    fun getHomeFeedReturnsNonEmptySections() {
        val feed = repository.getHomeFeed()
        assertTrue(feed.isNotEmpty())
        assertTrue(feed.all { it.albums.isNotEmpty() })
    }

    @Test
    fun getPlaylistReturnsMatchingId() {
        val playlist = repository.getPlaylist(1)
        assertEquals(1, playlist?.id)
        assertTrue(playlist!!.songs.isNotEmpty())
    }

    @Test
    fun getPlaylistReturnsNullForUnknownId() {
        val playlist = repository.getPlaylist(999)
        assertEquals(null, playlist)
    }
}
