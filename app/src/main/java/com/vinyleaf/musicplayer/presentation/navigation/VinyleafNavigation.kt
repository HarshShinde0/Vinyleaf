package com.vinyleaf.musicplayer.presentation.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.vinyleaf.musicplayer.presentation.screen.home.HomeScreen
import com.vinyleaf.musicplayer.presentation.screen.library.LibraryScreen
import com.vinyleaf.musicplayer.presentation.screen.player.PlayerScreen
import com.vinyleaf.musicplayer.presentation.screen.search.SearchScreen
import com.vinyleaf.musicplayer.presentation.screen.settings.SettingsScreen
import com.vinyleaf.musicplayer.presentation.screen.auth.GoogleDriveAuthScreen
import com.vinyleaf.musicplayer.presentation.component.BottomNavigationBar
import com.vinyleaf.musicplayer.presentation.component.MiniPlayer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VinyleafNavigation(
    navController: NavHostController = rememberNavController()
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    val showBottomBar = currentRoute != Screen.Player.route && currentRoute != Screen.Auth.route

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                Column {
                    // Mini Player
                    MiniPlayer(
                        title = "Currently Playing Song",
                        artist = "Artist Name",
                        isPlaying = true,
                        progress = 0.6f,
                        onMiniPlayerClick = {
                            navController.navigate(Screen.Player.route)
                        }
                    )

                    // Bottom Navigation
                    BottomNavigationBar(navController = navController)
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Auth.route, // Start with authentication
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Auth.route) {
                GoogleDriveAuthScreen(
                    onAuthSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Auth.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.Home.route) {
                HomeScreen(navController = navController)
            }
            
            composable(Screen.Library.route) {
                LibraryScreen(navController = navController)
            }
            
            composable(Screen.Search.route) {
                SearchScreen(navController = navController)
            }
            
            composable(Screen.Settings.route) {
                SettingsScreen(navController = navController)
            }
            
            composable(Screen.Player.route) {
                PlayerScreen(navController = navController)
            }
        }
    }
}
