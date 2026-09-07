package com.example.spotify.repository

import com.example.spotify.datamodel.Playlist
import com.example.spotify.network.NetworkApi
import javax.inject.Inject

/** Fetches an album's song list from the backend. */
class PlaylistRepository @Inject constructor(private val networkApi: NetworkApi) {

    suspend fun getPlaylist(id: Int): Playlist = networkApi.getPlaylist(id)
}
