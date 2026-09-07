package com.example.spotify.ui.playlist

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.spotify.util.AlbumAsyncImage
import com.example.spotify.R
import com.example.spotify.datamodel.Album
import com.example.spotify.datamodel.Song
import com.example.spotify.player.PlayerUiState
import com.example.spotify.player.PlayerViewModel

/** Album detail screen: cover art, favorite toggle, and the song list. */
@Composable
fun PlaylistScreen(
    playlistViewModel: PlaylistViewModel,
    playerViewModel: PlayerViewModel
) {
    val playlistUiState by playlistViewModel.uiState.collectAsState()
    val playerUiState by playerViewModel.uiState.collectAsState()

    PlaylistScreenContent(playlistUiState = playlistUiState,
        playerUiState = playerUiState,
        onTapFavorite = { isFavorite ->
            playlistViewModel.toggleFavorite(isFavorite)
        },
        onTapSong = { song ->
            playerViewModel.load(song, playlistUiState.album)
            playerViewModel.play()
        })
}

@Composable
private fun PlaylistScreenContent(
    playlistUiState: PlaylistUiState,
    playerUiState: PlayerUiState,
    onTapFavorite: (Boolean) -> Unit,
    onTapSong: (Song) -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {

        Cover(
            album = playlistUiState.album,
            isFavorite = playlistUiState.isFavorite,
            isPlaying = playerUiState.isPlaying,
            onTapFavorite = onTapFavorite,
        )

        PlaylistHeader(album = playlistUiState.album)

        if (playlistUiState.errorMessage != null) {
            ErrorMessage(playlistUiState.errorMessage)
        } else {
            PlaylistContent(
                playlist = playlistUiState.playlist,
                currentSong = playerUiState.song,
                onTapSong = onTapSong
            )
        }
    }
}

@Composable
private fun ErrorMessage(message: String) {
    Text(
        text = message,
        modifier = Modifier.padding(vertical = 16.dp),
        style = MaterialTheme.typography.body2,
        color = Color.LightGray,
    )
}

@Composable
private fun PlaylistContent(
    playlist: List<Song>,
    currentSong: Song? = null,
    onTapSong: (Song) -> Unit
) {
    val state = rememberLazyListState()
    LazyColumn(state = state) {
        items(playlist) { song ->
            Song(
                song = song,
                isPlaying = song == currentSong,
                onTapSong = onTapSong
            )
        }

        item {
            Spacer(modifier = Modifier.height(40.dp))
        }
    }

}

@Composable
private fun Song(song: Song, isPlaying: Boolean, onTapSong: (Song) -> Unit) {
    Row(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .clickable {
                onTapSong(song)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1.0f)) {
            Text(
                text = song.name,
                style = MaterialTheme.typography.body2,
                color = if (isPlaying) {
                    Color.Green
                } else {
                    Color.White
                },
            )

            Text(
                text = song.lyric,
                style = MaterialTheme.typography.caption,
                color = Color.Gray,
            )
        }
        Text(
            text = song.length,
            modifier = Modifier.padding(start = 8.dp),
            style = MaterialTheme.typography.body2,
            color = Color.LightGray,
        )
    }
}

@Composable
private fun PlaylistHeader(album: Album) {
    Column {
        Text(
            text = album.name,
            style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(top = 16.dp),
            color = Color.White
        )

        Text(
            text = stringResource(id = R.string.album_info, album.artists, album.year),
            style = MaterialTheme.typography.body2,
            color = Color.LightGray,
        )
    }
}

@Composable
private fun Cover(
    album: Album,
    isFavorite: Boolean,
    isPlaying: Boolean,
    onTapFavorite: (Boolean) -> Unit,
) {
    var rotation by remember { mutableStateOf(0f) }

    val rotationAnimation = rememberInfiniteTransition()
    val rotationValue by rotationAnimation.animateFloat(
        initialValue = 0f,
        targetValue = -360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 5000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    if (isPlaying) {
        rotation = rotationValue
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                modifier = Modifier
                    .size(28.dp)
                    .align(Alignment.TopEnd)
                    .clickable {
                        onTapFavorite(!isFavorite)
                    },
                painter = painterResource(
                    id = if (isFavorite) {
                        R.drawable.ic_favorite_24
                    } else {
                        R.drawable.ic_unfavorite_24
                    }
                ),
                tint = if (isFavorite) {
                    Color.Green
                } else {
                    Color.Gray
                },
                contentDescription = ""
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .aspectRatio(1.0f)
                    .align(Alignment.Center)
            ) {
                // Vinyl background
                Image(
                    modifier = Modifier
                        .fillMaxSize(),
                    painter = painterResource(id = R.drawable.vinyl_background),
                    contentDescription = null
                )

                AlbumAsyncImage(
                    model = album.cover,
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .aspectRatio(1.0f)
                        .graphicsLayer(
                            rotationZ = rotation
                        )
                        .align(Alignment.Center)
                        .clip(CircleShape),
                    contentScale = ContentScale.FillBounds
                )
            }

        }

        Text(
            text = album.description,
            modifier = Modifier.padding(top = 4.dp),
            style = MaterialTheme.typography.caption,
            color = Color.Gray,
        )
    }
}
