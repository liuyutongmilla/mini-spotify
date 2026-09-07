package com.example.model

import kotlinx.serialization.Serializable

/**
 * Album metadata returned to the Android client. The `album` field name
 * (rather than `name`) matches the JSON key the Android app's Gson model
 * expects via `@SerializedName("album")`.
 */
@Serializable
data class Album(
    val id: Int,
    val album: String,
    val year: String,
    val cover: String,
    val artists: String,
    val description: String
)
