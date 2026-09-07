package com.example.spotify.network

import com.example.spotify.datamodel.Playlist
import com.example.spotify.datamodel.Section
import retrofit2.http.GET
import retrofit2.http.Path

/** Retrofit service describing the backend's REST endpoints. */
interface NetworkApi {
    @GET("feed")
    suspend fun getHomeFeed(): List<Section>

    @GET("playlist/{id}")
    suspend fun getPlaylist(@Path("id") id: Int): Playlist
}
