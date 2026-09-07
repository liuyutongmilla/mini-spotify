package com.example.spotify.repository

import com.example.spotify.database.AlbumDao
import com.example.spotify.datamodel.Album
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

/** Mediates between the Favorite feature and the local [AlbumDao]. */
class FavoriteAlbumRepository @Inject constructor(private val albumDao: AlbumDao) {

    fun isFavoriteAlbum(id: Int): Flow<Boolean> =
        albumDao.isFavoriteAlbum(id).flowOn(Dispatchers.IO)

    suspend fun favoriteAlbum(album: Album) = withContext(Dispatchers.IO) {
        albumDao.favoriteAlbum(album)
    }

    suspend fun unFavoriteAlbum(album: Album) = withContext(Dispatchers.IO) {
        albumDao.unFavoriteAlbum(album)
    }

    fun fetchFavoriteAlbums(): Flow<List<Album>> =
        albumDao.fetchFavoriteAlbums().flowOn(Dispatchers.IO)
}
