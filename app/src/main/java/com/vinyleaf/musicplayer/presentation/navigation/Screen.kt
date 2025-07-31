package com.vinyleaf.musicplayer.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val title: String,
    val icon: ImageVector? = null,
    val selectedIcon: ImageVector? = null
) {
    object Splash : Screen(
        route = "splash",
        title = "Splash"
    )

    object Onboarding : Screen(
        route = "onboarding",
        title = "Onboarding"
    )

    object Auth : Screen(
        route = "auth",
        title = "Authentication"
    )

    object Register : Screen(
        route = "register",
        title = "Register"
    )

    object Home : Screen(
        route = "home",
        title = "Home",
        icon = Icons.Outlined.Home,
        selectedIcon = Icons.Filled.Home
    )

    object Explore : Screen(
        route = "explore",
        title = "Explore",
        icon = Icons.Outlined.Explore,
        selectedIcon = Icons.Filled.Explore
    )
    
    object Library : Screen(
        route = "library", 
        title = "Library",
        icon = Icons.Outlined.LibraryMusic,
        selectedIcon = Icons.Filled.LibraryMusic
    )

    object Favorites : Screen(
        route = "favorites",
        title = "Favorites",
        icon = Icons.Outlined.Favorite,
        selectedIcon = Icons.Filled.Favorite
    )

    object Profile : Screen(
        route = "profile",
        title = "Profile",
        icon = Icons.Outlined.Person,
        selectedIcon = Icons.Filled.Person
    )
    
    object Search : Screen(
        route = "search",
        title = "Search",
        icon = Icons.Outlined.Search,
        selectedIcon = Icons.Filled.Search
    )
    
    object Settings : Screen(
        route = "settings",
        title = "Settings"
    )
    
    object Player : Screen(
        route = "player",
        title = "Now Playing"
    )
    
    object Playlist : Screen(
        route = "playlist/{playlistId}",
        title = "Playlist"
    ) {
        fun createRoute(playlistId: String) = "playlist/$playlistId"
    }
    
    object Artist : Screen(
        route = "artist/{artistName}",
        title = "Artist"
    ) {
        fun createRoute(artistName: String) = "artist/$artistName"
    }
    
    object Album : Screen(
        route = "album/{albumName}",
        title = "Album"
    ) {
        fun createRoute(albumName: String) = "album/$albumName"
    }
    
    companion object {
        val mainBottomNavItems = listOf(Home, Explore, Library, Favorites, Profile)
    }
}
