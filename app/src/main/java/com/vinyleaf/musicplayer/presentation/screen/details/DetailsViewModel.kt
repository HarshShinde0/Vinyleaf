package com.vinyleaf.musicplayer.presentation.screen.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DetailsUiState(
    val songDetails: SongDetails? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class DetailsViewModel @Inject constructor(
    // Inject your repositories here
    // private val musicRepository: MusicRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailsUiState())
    val uiState: StateFlow<DetailsUiState> = _uiState.asStateFlow()

    fun loadSongDetails(songId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                // TODO: Load actual song data from repository
                // For now, using mock data
                val mockSong = SongDetails(
                    id = songId,
                    title = "Blinding Lights",
                    artist = "The Weeknd",
                    album = "After Hours",
                    duration = "3:20",
                    albumArtUrl = null, // You can add actual album art URLs
                    genre = "Synthpop",
                    year = 2019,
                    bitrate = "320 kbps",
                    fileSize = "7.8 MB",
                    isFavorite = false,
                    playCount = 47,
                    addedDate = "Jan 15, 2024"
                )

                _uiState.value = _uiState.value.copy(
                    songDetails = mockSong,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error occurred"
                )
            }
        }
    }

    fun playSong() {
        viewModelScope.launch {
            try {
                // TODO: Implement play functionality
                // Start playing the current song
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            try {
                val currentSong = _uiState.value.songDetails ?: return@launch
                val updatedSong = currentSong.copy(isFavorite = !currentSong.isFavorite)

                // TODO: Update favorite status in repository

                _uiState.value = _uiState.value.copy(songDetails = updatedSong)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun shareSong() {
        viewModelScope.launch {
            try {
                // TODO: Implement share functionality
                // Create share intent with song details
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun downloadSong() {
        viewModelScope.launch {
            try {
                // TODO: Implement download functionality
                // Download song for offline playback
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }
}
