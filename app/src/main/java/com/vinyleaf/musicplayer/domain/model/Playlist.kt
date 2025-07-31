package com.vinyleaf.musicplayer.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Locale
import kotlinx.serialization.Serializable

@Entity(tableName = "playlists")
@Serializable
data class Playlist(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String? = null,
    val coverArtUrl: String? = null,
    val songCount: Int = 0,
    val duration: Long = 0, // total duration in milliseconds
    val dateCreated: Long = System.currentTimeMillis(),
    val dateModified: Long = System.currentTimeMillis(),
    val isSystemPlaylist: Boolean = false // for built-in playlists like "Recently Played", "Favorites"
) {
    companion object {
        const val FAVORITES_ID = "favorites"
        const val RECENTLY_PLAYED_ID = "recently_played"
        const val MOST_PLAYED_ID = "most_played"
        
        fun createFavoritesPlaylist() = Playlist(
            id = FAVORITES_ID,
            name = "Favorites",
            description = "Your favorite songs",
            isSystemPlaylist = true
        )
        
        fun createRecentlyPlayedPlaylist() = Playlist(
            id = RECENTLY_PLAYED_ID,
            name = "Recently Played",
            description = "Songs you've played recently",
            isSystemPlaylist = true
        )
        
        fun createMostPlayedPlaylist() = Playlist(
            id = MOST_PLAYED_ID,
            name = "Most Played",
            description = "Your most played songs",
            isSystemPlaylist = true
        )
    }
    
    fun getDurationFormatted(): String {
        val hours = duration / 3600000
        val minutes = (duration % 3600000) / 60000
        return when {
            hours > 0 -> String.format(Locale.getDefault(), "%d hr %d min", hours, minutes)
            minutes > 0 -> String.format(Locale.getDefault(), "%d min", minutes)
            else -> "0 min"
        }
    }
}
