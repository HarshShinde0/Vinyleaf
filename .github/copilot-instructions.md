<!-- Use this file to provide workspace-specific custom instructions to Copilot. For more details, visit https://code.visualstudio.com/docs/copilot/copilot-customization#_use-a-githubcopilotinstructionsmd-file -->

# Vinyleaf - Android Music Player App

This is an Android music player application built with Kotlin that integrates with Google Drive for cloud music streaming.

## Project Guidelines

### Architecture
- Follow MVVM architecture pattern with ViewModels and LiveData
- Use Repository pattern for data management
- Implement dependency injection with Hilt
- Use Room database for local caching

### UI/UX
- Follow Material Design 3 guidelines
- Use Jetpack Compose for modern UI components
- Implement dark/light theme support
- Create smooth animations and transitions
- Design should be inspired by modern music apps with clean, minimalist interface

### Google Drive Integration
- Use Google Drive API v3 for file access
- Implement OAuth 2.0 authentication
- Support audio file formats: MP3, FLAC, WAV, AAC, OGG
- Enable file metadata parsing and caching

### Music Player Features
- Background playback with MediaSession
- Notification controls and lock screen controls
- Shuffle, repeat, and queue management
- Audio focus handling
- Equalizer support
- Playlist creation and management

### Code Quality
- Use Kotlin coroutines for asynchronous operations
- Implement proper error handling and logging
- Write unit tests for business logic
- Follow Android best practices for memory management
- Use sealed classes for state management

### Dependencies to Consider
- Jetpack Compose for UI
- ExoPlayer for media playback
- Retrofit for network requests
- Google Drive API
- Room for local database
- Hilt for dependency injection
- Coil for image loading
- Material3 components
