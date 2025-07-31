package com.vinyleaf.musicplayer.data.remote.api

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.google.api.services.drive.model.File
import com.google.api.services.drive.model.FileList
import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoogleDriveService @Inject constructor(
    private val context: Context
) {
    
    private var driveService: Drive? = null
    private var credential: GoogleAccountCredential? = null
    
    companion object {
        private val SCOPES = listOf(DriveScopes.DRIVE_READONLY)
        private const val APPLICATION_NAME = "Vinyleaf Music Player"
        
        // Supported audio file types
        private val AUDIO_MIME_TYPES = setOf(
            "audio/mpeg",      // MP3
            "audio/flac",      // FLAC
            "audio/wav",       // WAV
            "audio/x-wav",     // WAV alternative
            "audio/aac",       // AAC
            "audio/mp4",       // M4A
            "audio/ogg",       // OGG
            "audio/vorbis"     // OGG Vorbis
        )
        
        private val AUDIO_EXTENSIONS = setOf(
            "mp3", "flac", "wav", "aac", "m4a", "ogg", "oga"
        )
    }
    
    fun initializeCredential(accountName: String) {
        credential = GoogleAccountCredential.usingOAuth2(context, SCOPES).apply {
            selectedAccountName = accountName
        }
        
        driveService = Drive.Builder(
            NetHttpTransport(),
            GsonFactory.getDefaultInstance(),
            credential
        )
            .setApplicationName(APPLICATION_NAME)
            .build()
    }
    
    suspend fun searchAudioFiles(
        pageToken: String? = null,
        pageSize: Int = 100
    ): Result<FileList> = withContext(Dispatchers.IO) {
        try {
            val service = driveService ?: return@withContext Result.failure(
                IllegalStateException("Drive service not initialized")
            )
            
            // Build query to search for audio files
            val mimeTypeQuery = AUDIO_MIME_TYPES.joinToString(" or ") { "mimeType='$it'" }
            val extensionQuery = AUDIO_EXTENSIONS.joinToString(" or ") { "name contains '.$it'" }
            val query = "($mimeTypeQuery or $extensionQuery) and trashed=false"
            
            val request = service.files().list().apply {
                q = query
                fields = "nextPageToken, files(id, name, size, mimeType, parents, createdTime, modifiedTime, webContentLink, thumbnailLink)"
                this.pageSize = pageSize
                pageToken?.let { this.pageToken = it }
                orderBy = "name"
            }
            
            val result = request.execute()
            Result.success(result)
            
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getFileMetadata(fileId: String): Result<File> = withContext(Dispatchers.IO) {
        try {
            val service = driveService ?: return@withContext Result.failure(
                IllegalStateException("Drive service not initialized")
            )
            val file = service.files().get(fileId).apply {
                fields = "id, name, size, mimeType, parents, createdTime, modifiedTime, webContentLink, thumbnailLink, properties"
            }.execute()
            
            Result.success(file)
            
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getFileDownloadUrl(fileId: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            val service = driveService ?: return@withContext Result.failure(
                IllegalStateException("Drive service not initialized")
            )
            
            val file = service.files().get(fileId).apply {
                fields = "webContentLink"
            }.execute()
            
            file.webContentLink?.let { url ->
                Result.success(url)
            } ?: Result.failure(Exception("No download URL available"))
            
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun downloadFile(fileId: String): Result<ByteArray> = withContext(Dispatchers.IO) {
        try {
            val service = driveService ?: return@withContext Result.failure(
                IllegalStateException("Drive service not initialized")
            )
            
            val outputStream = java.io.ByteArrayOutputStream()
            service.files().get(fileId).executeMediaAndDownloadTo(outputStream)
            
            Result.success(outputStream.toByteArray())
            
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getFolderContents(folderId: String): Result<FileList> = withContext(Dispatchers.IO) {
        try {
            val service = driveService ?: return@withContext Result.failure(
                IllegalStateException("Drive service not initialized")
            )
            
            val request = service.files().list().apply {
                q = "'$folderId' in parents and trashed=false"
                fields = "files(id, name, size, mimeType, parents, createdTime, modifiedTime)"
                orderBy = "folder,name"
            }
            
            val result = request.execute()
            Result.success(result)
            
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    fun isAudioFile(file: File): Boolean {
        return file.mimeType in AUDIO_MIME_TYPES || 
               AUDIO_EXTENSIONS.any { ext -> 
                   file.name.lowercase().endsWith(".$ext") 
               }
    }
    
    fun getCredential(): GoogleAccountCredential? = credential
    
    fun isInitialized(): Boolean = driveService != null && credential != null
}
