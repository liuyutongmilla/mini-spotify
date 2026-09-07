package com.example.spotify.datamodel

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * An album returned by the backend catalog and, when favorited, persisted
 * locally as a Room [Entity].
 */
@Entity
data class Album(
    @PrimaryKey
    val id: Int,
    @SerializedName("album")
    val name: String,
    val year: String,
    val cover: String,
    val artists: String,
    val description: String
) : Serializable {
    companion object {
        fun empty(): Album = Album(
            id = -1,
            name = "",
            year = "",
            cover = "",
            artists = "",
            description = "",
        )
    }
}
