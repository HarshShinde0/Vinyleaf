package com.vinyleaf.musicplayer.di

import android.content.Context
import androidx.room.Room
import com.vinyleaf.musicplayer.data.local.database.VinyleafDatabase
import com.vinyleaf.musicplayer.data.remote.api.GoogleDriveService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    fun provideVinyleafDatabase(
        @ApplicationContext context: Context
    ): VinyleafDatabase {
        return Room.databaseBuilder(
            context,
            VinyleafDatabase::class.java,
            VinyleafDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }
    
    @Provides
    fun provideSongDao(database: VinyleafDatabase) = database.songDao()
    
    @Provides
    fun providePlaylistDao(database: VinyleafDatabase) = database.playlistDao()
    
    @Provides
    @Singleton
    fun provideGoogleDriveService(
        @ApplicationContext context: Context
    ): GoogleDriveService {
        return GoogleDriveService(context)
    }
}
