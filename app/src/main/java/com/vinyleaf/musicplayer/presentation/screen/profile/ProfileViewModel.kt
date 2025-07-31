package com.vinyleaf.musicplayer.presentation.screen.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val userName: String = "",
    val userEmail: String = "",
    val totalSongs: Int = 0,
    val totalPlaylists: Int = 0,
    val totalListeningTime: String = "0",
    val favoriteGenre: String = "Pop",
    val streakDays: Int = 0,
    val thisWeekMinutes: Int = 0,
    val recentSongs: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    // Inject your repositories here
    // private val userRepository: UserRepository,
    // private val musicRepository: MusicRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                // TODO: Load actual user data from repositories
                // For now, using mock data
                _uiState.value = _uiState.value.copy(
                    userName = "Music Lover",
                    userEmail = "user@example.com",
                    totalSongs = 1247,
                    totalPlaylists = 12,
                    totalListeningTime = "156",
                    favoriteGenre = "Pop",
                    streakDays = 7,
                    thisWeekMinutes = 420,
                    recentSongs = listOf(
                        "Blinding Lights - The Weeknd",
                        "Watermelon Sugar - Harry Styles",
                        "Levitating - Dua Lipa",
                        "Good 4 U - Olivia Rodrigo",
                        "Heat Waves - Glass Animals"
                    ),
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                // TODO: Implement logout logic
                // Clear user session, revoke tokens, etc.
                _uiState.value = ProfileUiState() // Reset to initial state
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun refreshProfile() {
        loadUserProfile()
    }
}
