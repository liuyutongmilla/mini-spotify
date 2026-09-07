package com.example.model

import kotlinx.serialization.Serializable

/** A single playable track inside a [Playlist]. */
@Serializable
data class Song(
    val name: String,
    val lyric: String,
    val src: String,
    val length: String
)
