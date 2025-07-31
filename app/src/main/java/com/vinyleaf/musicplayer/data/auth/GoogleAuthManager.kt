package com.vinyleaf.musicplayer.data.auth

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.api.services.drive.DriveScopes
import com.vinyleaf.musicplayer.BuildConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoogleAuthManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private var googleSignInClient: GoogleSignInClient? = null

    companion object {
        const val RC_SIGN_IN = 9001
    }

    init {
        setupGoogleSignIn()
    }

    private fun setupGoogleSignIn() {
        val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestProfile()
            .requestIdToken(BuildConfig.GOOGLE_DRIVE_CLIENT_ID) // Your OAuth client ID
            .requestScopes(
                Scope("https://www.googleapis.com/auth/drive.readonly"),
                Scope("https://www.googleapis.com/auth/drive.file")
            )
            .build()

        googleSignInClient = GoogleSignIn.getClient(context, signInOptions)
    }

    /**
     * Check if user is already signed in
     */
    fun isSignedIn(): Boolean {
        val account = GoogleSignIn.getLastSignedInAccount(context)
        return account != null
    }

    /**
     * Get currently signed in account
     */
    fun getCurrentAccount(): GoogleSignInAccount? {
        return GoogleSignIn.getLastSignedInAccount(context)
    }

    /**
     * Get sign-in intent for user authentication
     */
    fun getSignInIntent(): Intent? {
        return googleSignInClient?.signInIntent
    }

    /**
     * Sign out user
     */
    suspend fun signOut(): Result<Unit> {
        return try {
            googleSignInClient?.signOut()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * For development testing - simulate successful authentication
     */
    fun simulateAuthForTesting(): GoogleSignInAccount? {
        // This is for UI testing only - returns mock account
        return GoogleSignIn.getLastSignedInAccount(context)
    }
}
