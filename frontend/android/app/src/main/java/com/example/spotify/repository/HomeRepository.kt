package com.example.spotify.repository

import com.example.spotify.datamodel.Section
import com.example.spotify.network.NetworkApi
import javax.inject.Inject

/** Fetches the home feed sections from the backend. */
class HomeRepository @Inject constructor(private val networkApi: NetworkApi) {

    suspend fun getHomeSections(): List<Section> = networkApi.getHomeFeed()
}
