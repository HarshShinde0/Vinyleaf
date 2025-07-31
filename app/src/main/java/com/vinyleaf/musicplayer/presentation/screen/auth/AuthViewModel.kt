package com.vinyleaf.musicplayer.presentation.screen.auth

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.vinyleaf.musicplayer.data.auth.GoogleAuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false,
    val error: String? = null,
    val userEmail: String? = null
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val googleAuthManager: GoogleAuthManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        checkExistingAuth()
    }

    private fun checkExistingAuth() {
        viewModelScope.launch {
            val isSignedIn = googleAuthManager.isSignedIn()
            val account = googleAuthManager.getCurrentAccount()

            _uiState.value = _uiState.value.copy(
                isAuthenticated = isSignedIn,
                userEmail = account?.email
            )
        }
    }

    fun getSignInIntent(): Intent? {
        return googleAuthManager.getSignInIntent()
    }

    fun handleSignInResult(account: GoogleSignInAccount?) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            if (account != null) {
                try {
                    // Account signed in successfully
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isAuthenticated = true,
                        userEmail = account.email,
                        error = null
                    )
                } catch (e: Exception) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Failed to authenticate: ${e.message}"
                    )
                }
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Sign in failed. Please try again."
                )
            }
        }
    }

    fun handleSignInError(exception: ApiException) {
        val errorMessage = when (exception.statusCode) {
            12501 -> "Sign in was cancelled"
            12502 -> "Sign in failed. Please check your network connection."
            16 -> "App not verified by Google yet - this is normal during development"
            else -> "Sign in failed: ${exception.message}"
        }

        _uiState.value = _uiState.value.copy(
            isLoading = false,
            error = errorMessage
        )
    }

    /**
     * For development testing - simulate successful authentication
     */
    fun simulateSuccessfulAuth() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            // Simulate loading
            kotlinx.coroutines.delay(1500)

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                isAuthenticated = true,
                userEmail = "test@gmail.com",
                error = null
            )
        }
    }

    fun signOut() {
        viewModelScope.launch {
            try {
                googleAuthManager.signOut()
                _uiState.value = _uiState.value.copy(
                    isAuthenticated = false,
                    userEmail = null,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to sign out: ${e.message}"
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
