package com.vinyleaf.musicplayer.presentation.screen.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vinyleaf.musicplayer.domain.model.Playlist
import com.vinyleaf.musicplayer.domain.model.Song
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ExploreUiState(
    val isLoading: Boolean = false,
    val featuredContent: List<Song> = emptyList(),
    val trendingSongs: List<Song> = emptyList(),
    val newReleases: List<Song> = emptyList(),
    val popularPlaylists: List<Playlist> = emptyList(),
    val genres: List<String> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class ExploreViewModel @Inject constructor(
    // TODO: Inject repositories and use cases when implemented
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExploreUiState())
    val uiState: StateFlow<ExploreUiState> = _uiState.asStateFlow()

    init {
        loadExploreContent()
    }

    private fun loadExploreContent() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                // Sample data for featured content
                val featuredSongs = listOf(
                    Song(
                        id = "featured_1",
                        title = "Midnight Vibes",
                        artist = "Luna Eclipse",
                        album = "Nocturnal Sessions",
                        duration = 234000,
                        driveFileId = "featured_drive_id_1",
                        mimeType = "audio/mpeg"
                    )
                )

                // Sample trending songs
                val trendingSongs = listOf(
                    Song(
                        id = "trending_1",
                        title = "Electric Dreams",
                        artist = "Neon Pulse",
                        album = "Synthwave Anthology",
                        duration = 198000,
                        driveFileId = "trending_drive_id_1",
                        mimeType = "audio/mpeg"
                    ),
                    Song(
                        id = "trending_2",
                        title = "Ocean Waves",
                        artist = "Serenity Sound",
                        album = "Nature's Symphony",
                        duration = 267000,
                        driveFileId = "trending_drive_id_2",
                        mimeType = "audio/mpeg"
                    ),
                    Song(
                        id = "trending_3",
                        title = "Urban Jungle",
                        artist = "City Beats",
                        album = "Metropolitan",
                        duration = 189000,
                        driveFileId = "trending_drive_id_3",
                        mimeType = "audio/mpeg"
                    )
                )

                // Sample new releases
                val newReleases = listOf(
                    Song(
                        id = "new_1",
                        title = "Rising Sun",
                        artist = "Dawn Chorus",
                        album = "Morning Glory",
                        duration = 201000,
                        driveFileId = "new_drive_id_1",
                        mimeType = "audio/mpeg"
                    ),
                    Song(
                        id = "new_2",
                        title = "Starlight",
                        artist = "Cosmic Ray",
                        album = "Galaxy Tales",
                        duration = 223000,
                        driveFileId = "new_drive_id_2",
                        mimeType = "audio/mpeg"
                    )
                )

                // Sample popular playlists
                val popularPlaylists = listOf(
                    Playlist(
                        id = "playlist_1",
                        name = "Chill Vibes",
                        description = "Perfect for relaxing",
                        songCount = 24
                    ),
                    Playlist(
                        id = "playlist_2",
                        name = "Workout Mix",
                        description = "High energy tracks",
                        songCount = 18
                    ),
                    Playlist(
                        id = "playlist_3",
                        name = "Late Night Jazz",
                        description = "Smooth jazz for evening",
                        songCount = 31
                    ),
                    Playlist(
                        id = "playlist_4",
                        name = "Indie Favorites",
                        description = "Best indie tracks",
                        songCount = 42
                    )
                )

                // Sample genres
                val genres = listOf(
                    "Rock", "Pop", "Jazz", "Classical", "Electronic",
                    "Hip Hop", "Country", "R&B", "Indie", "Alternative"
                )

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    featuredContent = featuredSongs,
                    trendingSongs = trendingSongs,
                    newReleases = newReleases,
                    popularPlaylists = popularPlaylists,
                    genres = genres,
                    error = null
                )

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load explore content"
                )
            }
        }
    }

    fun refreshContent() {
        loadExploreContent()
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
