package com.example.model

import kotlinx.serialization.Serializable

/** The song list for one album, keyed by album id. */
@Serializable
data class Playlist(
    val id: Int,
    val songs: List<Song>
)
