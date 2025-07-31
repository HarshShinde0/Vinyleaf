package com.vinyleaf.musicplayer.data.local.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.vinyleaf.musicplayer.data.local.dao.PlaylistDao
import com.vinyleaf.musicplayer.data.local.dao.SongDao
import com.vinyleaf.musicplayer.domain.model.Playlist
import com.vinyleaf.musicplayer.domain.model.PlaylistSongCrossRef
import com.vinyleaf.musicplayer.domain.model.Song

@Database(
    entities = [
        Song::class,
        Playlist::class,
        PlaylistSongCrossRef::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class VinyleafDatabase : RoomDatabase() {
    
    abstract fun songDao(): SongDao
    abstract fun playlistDao(): PlaylistDao
    
    companion object {
        const val DATABASE_NAME = "vinyleaf_database"
        
        @Volatile
        private var INSTANCE: VinyleafDatabase? = null
        
        fun getDatabase(context: Context): VinyleafDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VinyleafDatabase::class.java,
                    DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
