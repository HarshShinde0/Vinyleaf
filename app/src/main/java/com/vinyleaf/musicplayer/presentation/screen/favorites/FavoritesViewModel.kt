package com.vinyleaf.musicplayer.presentation.screen.favorites

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

data class FavoritesUiState(
    val isLoading: Boolean = false,
    val favoriteSongs: List<Song> = emptyList(),
    val favoriteAlbums: List<String> = emptyList(),
    val favoriteArtists: List<String> = emptyList(),
    val favoritePlaylists: List<Playlist> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    // TODO: Inject repositories and use cases when implemented
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    init {
        loadFavorites()
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                // Sample favorite songs
                val favoriteSongs = listOf(
                    Song(
                        id = "fav_song_1",
                        title = "Dreams Come True",
                        artist = "Harmony Wave",
                        album = "Eternal Echoes",
                        duration = 245000,
                        driveFileId = "fav_drive_id_1",
                        mimeType = "audio/mpeg",
                        isFavorite = true
                    ),
                    Song(
                        id = "fav_song_2",
                        title = "Sunset Boulevard",
                        artist = "Golden Hour",
                        album = "City Lights",
                        duration = 198000,
                        driveFileId = "fav_drive_id_2",
                        mimeType = "audio/mpeg",
                        isFavorite = true
                    ),
                    Song(
                        id = "fav_song_3",
                        title = "Moonlight Serenade",
                        artist = "Night Jazz Collective",
                        album = "After Midnight",
                        duration = 312000,
                        driveFileId = "fav_drive_id_3",
                        mimeType = "audio/mpeg",
                        isFavorite = true
                    )
                )

                // Sample favorite albums
                val favoriteAlbums = listOf(
                    "Eternal Echoes",
                    "City Lights",
                    "After Midnight",
                    "Ocean Dreams"
                )

                // Sample favorite artists
                val favoriteArtists = listOf(
                    "Harmony Wave",
                    "Golden Hour",
                    "Night Jazz Collective",
                    "Acoustic Soul"
                )

                // Sample favorite playlists
                val favoritePlaylists = listOf(
                    Playlist(
                        id = "fav_playlist_1",
                        name = "My Chill Mix",
                        description = "Perfect for relaxing",
                        songCount = 28
                    ),
                    Playlist(
                        id = "fav_playlist_2",
                        name = "Road Trip Anthems",
                        description = "Great for long drives",
                        songCount = 45
                    )
                )

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    favoriteSongs = favoriteSongs,
                    favoriteAlbums = favoriteAlbums,
                    favoriteArtists = favoriteArtists,
                    favoritePlaylists = favoritePlaylists,
                    error = null
                )

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load favorites"
                )
            }
        }
    }

    fun playSong(song: Song) {
        viewModelScope.launch {
            try {
                // TODO: Implement song playback using music service
                // For now, just log the action
                println("Playing song: ${song.title} by ${song.artist}")
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to play song: ${e.message}"
                )
            }
        }
    }

    fun removeFromFavorites(song: Song) {
        viewModelScope.launch {
            try {
                // TODO: Implement remove from favorites using repository
                val updatedFavorites = _uiState.value.favoriteSongs.filterNot { it.id == song.id }
                _uiState.value = _uiState.value.copy(favoriteSongs = updatedFavorites)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to remove from favorites: ${e.message}"
                )
            }
        }
    }

    fun shuffleAllFavorites() {
        viewModelScope.launch {
            try {
                // TODO: Implement shuffle all favorites using music service
                val shuffledSongs = _uiState.value.favoriteSongs.shuffled()
                println("Shuffling ${shuffledSongs.size} favorite songs")
                // Start playback with shuffled queue
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to shuffle favorites: ${e.message}"
                )
            }
        }
    }

    fun refreshFavorites() {
        loadFavorites()
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
