package com.vinyleaf.musicplayer.presentation.screen.details

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.vinyleaf.musicplayer.presentation.theme.*

data class SongDetails(
    val id: String,
    val title: String,
    val artist: String,
    val album: String,
    val duration: String,
    val albumArtUrl: String? = null,
    val genre: String = "",
    val year: Int = 0,
    val bitrate: String = "",
    val fileSize: String = "",
    val isFavorite: Boolean = false,
    val playCount: Int = 0,
    val addedDate: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongDetailsScreen(
    songId: String,
    navController: NavController,
    viewModel: DetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showOptions by remember { mutableStateOf(false) }

    LaunchedEffect(songId) {
        viewModel.loadSongDetails(songId)
    }

    when {
        uiState.isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        uiState.error != null -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Outlined.Error,
                        contentDescription = "Error",
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = uiState.error ?: "Unknown error",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
        uiState.songDetails != null -> {
            SongDetailsContent(
                song = uiState.songDetails!!,
                onBackClick = { navController.navigateUp() },
                onPlayClick = { viewModel.playSong() },
                onFavoriteClick = { viewModel.toggleFavorite() },
                onShareClick = { viewModel.shareSong() },
                onAddToPlaylistClick = { /* Handle add to playlist */ },
                onDownloadClick = { viewModel.downloadSong() }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SongDetailsContent(
    song: SongDetails,
    onBackClick: () -> Unit,
    onPlayClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onShareClick: () -> Unit,
    onAddToPlaylistClick: () -> Unit,
    onDownloadClick: () -> Unit
) {
    // Dynamic gradient based on album art colors
    val gradientColors = listOf(
        MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
        MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f),
        MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f),
        MaterialTheme.colorScheme.background.copy(alpha = 0.3f),
        MaterialTheme.colorScheme.background
    )

    Box(modifier = Modifier.fillMaxSize()) {
        // Background with gradient
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(gradientColors)
                )
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            // Header with album art
            item {
                SongDetailsHeader(
                    song = song,
                    onBackClick = onBackClick
                )
            }

            // Action buttons
            item {
                Spacer(modifier = Modifier.height(24.dp))
                ActionButtonsRow(
                    isFavorite = song.isFavorite,
                    onPlayClick = onPlayClick,
                    onFavoriteClick = onFavoriteClick,
                    onShareClick = onShareClick,
                    onAddToPlaylistClick = onAddToPlaylistClick,
                    onDownloadClick = onDownloadClick
                )
            }

            // Song information
            item {
                Spacer(modifier = Modifier.height(24.dp))
                SongInformationCard(song = song)
            }

            // Audio properties
            item {
                Spacer(modifier = Modifier.height(16.dp))
                AudioPropertiesCard(song = song)
            }

            // Statistics
            item {
                Spacer(modifier = Modifier.height(16.dp))
                StatisticsCard(song = song)
            }
        }
    }
}

@Composable
private fun SongDetailsHeader(
    song: SongDetails,
    onBackClick: () -> Unit
) {
    Box {
        // Album art background (blurred)
        if (song.albumArtUrl != null) {
            AsyncImage(
                model = song.albumArtUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .blur(20.dp),
                contentScale = ContentScale.Crop,
                alpha = 0.3f
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Top bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.8f))
                ) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                IconButton(
                    onClick = { /* Handle more options */ },
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.8f))
                ) {
                    Icon(
                        Icons.Default.MoreVert,
                        contentDescription = "More",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Album art
            Card(
                modifier = Modifier
                    .size(280.dp)
                    .align(Alignment.CenterHorizontally),
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                if (song.albumArtUrl != null) {
                    AsyncImage(
                        model = song.albumArtUrl,
                        contentDescription = "Album Art",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.linearGradient(
                                    colors = PlaylistColors.take(3)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.MusicNote,
                            contentDescription = "Music",
                            modifier = Modifier.size(80.dp),
                            tint = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Song info
            Text(
                text = song.title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = song.artist,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = song.album,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
private fun ActionButtonsRow(
    isFavorite: Boolean,
    onPlayClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onShareClick: () -> Unit,
    onAddToPlaylistClick: () -> Unit,
    onDownloadClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        // Favorite button
        ActionButton(
            icon = if (isFavorite) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
            text = "Favorite",
            color = if (isFavorite) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onSurfaceVariant,
            onClick = onFavoriteClick
        )

        // Play button (larger)
        FloatingActionButton(
            onClick = onPlayClick,
            containerColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(64.dp)
        ) {
            Icon(
                Icons.Default.PlayArrow,
                contentDescription = "Play",
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }

        // Share button
        ActionButton(
            icon = Icons.Outlined.Share,
            text = "Share",
            onClick = onShareClick
        )
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Secondary actions
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 48.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        SecondaryActionButton(
            icon = Icons.Outlined.PlaylistAdd,
            text = "Add to Playlist",
            onClick = onAddToPlaylistClick
        )

        SecondaryActionButton(
            icon = Icons.Outlined.Download,
            text = "Download",
            onClick = onDownloadClick
        )
    }
}

@Composable
private fun ActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    color: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.8f))
        ) {
            Icon(
                icon,
                contentDescription = text,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
private fun SecondaryActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.height(40.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
        )
    ) {
        Icon(
            icon,
            contentDescription = text,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun SongInformationCard(song: SongDetails) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
        )
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Song Information",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(16.dp))

            InfoRow("Duration", song.duration)
            InfoRow("Genre", song.genre.takeIf { it.isNotEmpty() } ?: "Unknown")
            InfoRow("Year", if (song.year > 0) song.year.toString() else "Unknown")
            InfoRow("Album", song.album)
            InfoRow("Added", song.addedDate.takeIf { it.isNotEmpty() } ?: "Unknown")
        }
    }
}

@Composable
private fun AudioPropertiesCard(song: SongDetails) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
        )
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Audio Properties",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(16.dp))

            InfoRow("Bitrate", song.bitrate.takeIf { it.isNotEmpty() } ?: "Unknown")
            InfoRow("File Size", song.fileSize.takeIf { it.isNotEmpty() } ?: "Unknown")
            InfoRow("Format", "MP3") // You can make this dynamic
            InfoRow("Sample Rate", "44.1 kHz") // You can make this dynamic
        }
    }
}

@Composable
private fun StatisticsCard(song: SongDetails) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
        )
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Statistics",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(16.dp))

            InfoRow("Play Count", song.playCount.toString())
            InfoRow("Last Played", "2 hours ago") // You can make this dynamic
            InfoRow("Rating", "★★★★☆") // You can make this dynamic
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Medium
        )
    }
}
