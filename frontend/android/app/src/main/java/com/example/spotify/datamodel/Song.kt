package com.example.spotify.datamodel

/** A single playable track inside a [Playlist]. */
data class Song(
    val name: String,
    val lyric: String,
    val src: String,
    val length: String
)
