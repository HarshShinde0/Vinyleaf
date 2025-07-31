package com.vinyleaf.musicplayer.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Locale
import kotlinx.serialization.Serializable

@Entity(tableName = "songs")
@Serializable
data class Song(
    @PrimaryKey
    val id: String,
    val title: String,
    val artist: String,
    val album: String,
    val duration: Long, // in milliseconds
    val driveFileId: String,
    val downloadUrl: String? = null,
    val albumArtUrl: String? = null,
    val genre: String? = null,
    val year: Int? = null,
    val trackNumber: Int? = null,
    val bitrate: Int? = null,
    val fileSize: Long = 0,
    val mimeType: String,
    val dateAdded: Long = System.currentTimeMillis(),
    val lastPlayed: Long? = null,
    val playCount: Int = 0,
    val isFavorite: Boolean = false,
    val isDownloaded: Boolean = false,
    val localPath: String? = null
) {
    fun getDisplayArtist(): String = if (artist.isBlank()) "Unknown Artist" else artist
    fun getDisplayAlbum(): String = if (album.isBlank()) "Unknown Album" else album
    fun getDisplayTitle(): String = if (title.isBlank()) "Unknown Title" else title
    
    fun getDurationFormatted(): String {
        val minutes = duration / 60000
        val seconds = (duration % 60000) / 1000
        return String.format(Locale.getDefault(), "%d:%02d", minutes, seconds)
    }
    
    fun getFileSizeFormatted(): String {
        return when {
            fileSize >= 1024 * 1024 -> String.format(Locale.getDefault(), "%.1f MB", fileSize / (1024.0 * 1024.0))
            fileSize >= 1024 -> String.format(Locale.getDefault(), "%.1f KB", fileSize / 1024.0)
            else -> "$fileSize B"
        }
    }
}
