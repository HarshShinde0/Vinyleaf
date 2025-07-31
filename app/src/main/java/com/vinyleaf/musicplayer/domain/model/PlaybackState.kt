package com.vinyleaf.musicplayer.domain.model

sealed class PlayerState {
    object Idle : PlayerState()
    object Loading : PlayerState()
    object Playing : PlayerState()
    object Paused : PlayerState()
    object Stopped : PlayerState()
    data class Error(val message: String) : PlayerState()
}

enum class RepeatMode {
    OFF, ONE, ALL
}

enum class ShuffleMode {
    OFF, ON
}

data class PlaybackState(
    val currentSong: Song? = null,
    val playerState: PlayerState = PlayerState.Idle,
    val currentPosition: Long = 0,
    val duration: Long = 0,
    val playbackSpeed: Float = 1.0f,
    val repeatMode: RepeatMode = RepeatMode.OFF,
    val shuffleMode: ShuffleMode = ShuffleMode.OFF,
    val queue: List<Song> = emptyList(),
    val currentIndex: Int = -1,
    val isBuffering: Boolean = false
) {
    val progress: Float
        get() = if (duration > 0) currentPosition.toFloat() / duration.toFloat() else 0f
    
    val hasNext: Boolean
        get() = when (repeatMode) {
            RepeatMode.ONE -> true
            RepeatMode.ALL -> queue.isNotEmpty()
            RepeatMode.OFF -> currentIndex < queue.size - 1
        }
    
    val hasPrevious: Boolean
        get() = when (repeatMode) {
            RepeatMode.ONE -> true
            RepeatMode.ALL -> queue.isNotEmpty()
            RepeatMode.OFF -> currentIndex > 0
        }
}
