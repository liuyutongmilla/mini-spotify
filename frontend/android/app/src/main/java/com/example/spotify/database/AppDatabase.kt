package com.example.spotify.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.spotify.datamodel.Album

/** Local Room database used to persist the user's favorited albums. */
@Database(entities = [Album::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun albumDao(): AlbumDao
}
