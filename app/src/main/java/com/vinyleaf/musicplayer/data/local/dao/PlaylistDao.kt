package com.vinyleaf.musicplayer.data.local.dao

import androidx.room.*
import com.vinyleaf.musicplayer.domain.model.Playlist
import com.vinyleaf.musicplayer.domain.model.PlaylistSongCrossRef
import com.vinyleaf.musicplayer.domain.model.PlaylistWithSongs
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {
    
    @Query("SELECT * FROM playlists ORDER BY name ASC")
    fun getAllPlaylists(): Flow<List<Playlist>>
    
    @Query("SELECT * FROM playlists WHERE id = :id")
    suspend fun getPlaylistById(id: String): Playlist?
    
    @Transaction
    @Query("SELECT * FROM playlists WHERE id = :id")
    suspend fun getPlaylistWithSongs(id: String): PlaylistWithSongs?
    
    @Transaction
    @Query("SELECT * FROM playlists ORDER BY name ASC")
    fun getAllPlaylistsWithSongs(): Flow<List<PlaylistWithSongs>>
    
    @Query("SELECT * FROM playlists WHERE name LIKE '%' || :query || '%'")
    fun searchPlaylists(query: String): Flow<List<Playlist>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: Playlist)
    
    @Update
    suspend fun updatePlaylist(playlist: Playlist)
    
    @Delete
    suspend fun deletePlaylist(playlist: Playlist)
    
    @Query("DELETE FROM playlists WHERE id = :playlistId")
    suspend fun deletePlaylistById(playlistId: String)
    
    // Playlist-Song relationship operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylistSongCrossRef(crossRef: PlaylistSongCrossRef)
    
    @Delete
    suspend fun deletePlaylistSongCrossRef(crossRef: PlaylistSongCrossRef)
    
    @Query("DELETE FROM playlist_song_cross_ref WHERE playlistId = :playlistId AND songId = :songId")
    suspend fun removeSongFromPlaylist(playlistId: String, songId: String)
    
    @Query("DELETE FROM playlist_song_cross_ref WHERE playlistId = :playlistId")
    suspend fun removeAllSongsFromPlaylist(playlistId: String)
    
    @Query("SELECT COUNT(*) FROM playlist_song_cross_ref WHERE playlistId = :playlistId")
    suspend fun getPlaylistSongCount(playlistId: String): Int
    
    @Query("""
        UPDATE playlists 
        SET songCount = (
            SELECT COUNT(*) 
            FROM playlist_song_cross_ref 
            WHERE playlistId = :playlistId
        ),
        duration = (
            SELECT COALESCE(SUM(songs.duration), 0)
            FROM playlist_song_cross_ref
            JOIN songs ON playlist_song_cross_ref.songId = songs.id
            WHERE playlist_song_cross_ref.playlistId = :playlistId
        ),
        dateModified = :timestamp
        WHERE id = :playlistId
    """)
    suspend fun updatePlaylistStats(playlistId: String, timestamp: Long = System.currentTimeMillis())
    
    @Transaction
    suspend fun addSongToPlaylist(playlistId: String, songId: String) {
        val maxOrder = getMaxOrderInPlaylist(playlistId) ?: -1
        insertPlaylistSongCrossRef(
            PlaylistSongCrossRef(
                playlistId = playlistId,
                songId = songId,
                order = maxOrder + 1
            )
        )
        updatePlaylistStats(playlistId)
    }
    
    @Query("SELECT COALESCE(MAX(`order`), -1) FROM playlist_song_cross_ref WHERE playlistId = :playlistId")
    suspend fun getMaxOrderInPlaylist(playlistId: String): Int?
}
