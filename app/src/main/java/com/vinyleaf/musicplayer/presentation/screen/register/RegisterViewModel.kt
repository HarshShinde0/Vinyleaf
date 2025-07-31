package com.vinyleaf.musicplayer.presentation.screen.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RegisterUiState(
    val isLoading: Boolean = false,
    val isRegistered: Boolean = false,
    val error: String? = null,
    val user: User? = null
)

data class User(
    val id: String,
    val email: String,
    val username: String,
    val profileImageUrl: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

@HiltViewModel
class RegisterViewModel @Inject constructor(
    // TODO: Inject user repository when implemented
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun register(email: String, username: String, password: String, confirmPassword: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                // Validation
                when {
                    email.isBlank() -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = "Email is required"
                        )
                        return@launch
                    }
                    !isValidEmail(email) -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = "Please enter a valid email address"
                        )
                        return@launch
                    }
                    username.isBlank() -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = "Username is required"
                        )
                        return@launch
                    }
                    username.length < 3 -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = "Username must be at least 3 characters"
                        )
                        return@launch
                    }
                    password.length < 6 -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = "Password must be at least 6 characters"
                        )
                        return@launch
                    }
                    password != confirmPassword -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = "Passwords don't match"
                        )
                        return@launch
                    }
                }

                // Simulate registration process
                kotlinx.coroutines.delay(2000)

                // TODO: Implement actual registration with your backend/Firebase
                val user = User(
                    id = generateUserId(),
                    email = email,
                    username = username
                )

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isRegistered = true,
                    user = user,
                    error = null
                )

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Registration failed. Please try again."
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun generateUserId(): String {
        return "user_${System.currentTimeMillis()}_${(1000..9999).random()}"
    }
}
