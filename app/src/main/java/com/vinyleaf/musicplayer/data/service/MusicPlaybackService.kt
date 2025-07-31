package com.vinyleaf.musicplayer.data.service

import android.content.Intent
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@UnstableApi
class MusicPlaybackService : MediaSessionService() {
    
    private var mediaSession: MediaSession? = null
    
    override fun onCreate() {
        super.onCreate()
        // TODO: Initialize ExoPlayer and MediaSession
        // TODO: Set up notification and media controls
    }
    
    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // TODO: Handle media button intents
        return super.onStartCommand(intent, flags, startId)
    }
    
    override fun onDestroy() {
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }
        super.onDestroy()
    }
}
