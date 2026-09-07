package com.example.spotify.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.spotify.datamodel.Album
import kotlinx.coroutines.flow.Flow

/** Room DAO for the user's locally favorited albums. */
@Dao
interface AlbumDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun favoriteAlbum(album: Album)

    @Query("SELECT EXISTS(SELECT * FROM Album WHERE id = :id)")
    fun isFavoriteAlbum(id: Int): Flow<Boolean>

    @Delete
    suspend fun unFavoriteAlbum(album: Album)

    @Query("SELECT * FROM Album")
    fun fetchFavoriteAlbums(): Flow<List<Album>>
}
