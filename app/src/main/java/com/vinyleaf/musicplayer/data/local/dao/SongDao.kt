package com.vinyleaf.musicplayer.data.local.dao

import androidx.room.*
import com.vinyleaf.musicplayer.domain.model.Song
import kotlinx.coroutines.flow.Flow

@Dao
interface SongDao {
    
    @Query("SELECT * FROM songs ORDER BY title ASC")
    fun getAllSongs(): Flow<List<Song>>
    
    @Query("SELECT * FROM songs WHERE id = :id")
    suspend fun getSongById(id: String): Song?
    
    @Query("SELECT * FROM songs WHERE artist = :artist ORDER BY album, trackNumber ASC")
    fun getSongsByArtist(artist: String): Flow<List<Song>>
    
    @Query("SELECT * FROM songs WHERE album = :album ORDER BY trackNumber ASC")
    fun getSongsByAlbum(album: String): Flow<List<Song>>
    
    @Query("SELECT * FROM songs WHERE genre = :genre ORDER BY title ASC")
    fun getSongsByGenre(genre: String): Flow<List<Song>>
    
    @Query("SELECT * FROM songs WHERE title LIKE '%' || :query || '%' OR artist LIKE '%' || :query || '%' OR album LIKE '%' || :query || '%'")
    fun searchSongs(query: String): Flow<List<Song>>
    
    @Query("SELECT * FROM songs WHERE isFavorite = 1 ORDER BY title ASC")
    fun getFavoriteSongs(): Flow<List<Song>>
    
    @Query("SELECT * FROM songs WHERE lastPlayed IS NOT NULL ORDER BY lastPlayed DESC LIMIT :limit")
    fun getRecentlyPlayedSongs(limit: Int = 50): Flow<List<Song>>
    
    @Query("SELECT * FROM songs WHERE playCount > 0 ORDER BY playCount DESC, lastPlayed DESC LIMIT :limit")
    fun getMostPlayedSongs(limit: Int = 50): Flow<List<Song>>
    
    @Query("SELECT DISTINCT artist FROM songs WHERE artist != '' ORDER BY artist ASC")
    fun getAllArtists(): Flow<List<String>>
    
    @Query("SELECT DISTINCT album FROM songs WHERE album != '' ORDER BY album ASC")
    fun getAllAlbums(): Flow<List<String>>
    
    @Query("SELECT DISTINCT genre FROM songs WHERE genre IS NOT NULL AND genre != '' ORDER BY genre ASC")
    fun getAllGenres(): Flow<List<String>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSong(song: Song)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSongs(songs: List<Song>)
    
    @Update
    suspend fun updateSong(song: Song)
    
    @Query("UPDATE songs SET isFavorite = :isFavorite WHERE id = :songId")
    suspend fun updateFavoriteStatus(songId: String, isFavorite: Boolean)
    
    @Query("UPDATE songs SET lastPlayed = :timestamp, playCount = playCount + 1 WHERE id = :songId")
    suspend fun updatePlayStats(songId: String, timestamp: Long = System.currentTimeMillis())
    
    @Query("UPDATE songs SET isDownloaded = :isDownloaded, localPath = :localPath WHERE id = :songId")
    suspend fun updateDownloadStatus(songId: String, isDownloaded: Boolean, localPath: String?)
    
    @Delete
    suspend fun deleteSong(song: Song)
    
    @Query("DELETE FROM songs WHERE id = :songId")
    suspend fun deleteSongById(songId: String)
    
    @Query("DELETE FROM songs")
    suspend fun deleteAllSongs()
    
    @Query("SELECT COUNT(*) FROM songs")
    suspend fun getSongCount(): Int
    
    @Query("SELECT SUM(duration) FROM songs")
    suspend fun getTotalDuration(): Long?
    
    @Query("SELECT SUM(fileSize) FROM songs")
    suspend fun getTotalFileSize(): Long?
}
