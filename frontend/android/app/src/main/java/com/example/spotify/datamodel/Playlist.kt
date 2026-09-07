package com.example.spotify.datamodel

import com.google.gson.annotations.SerializedName

/** The song list for one album, keyed by album id. */
data class Playlist(
    @SerializedName("id")
    val albumId: Int,
    val songs: List<Song>
)
