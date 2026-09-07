package com.example.spotify.player

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.example.spotify.datamodel.Album
import com.example.spotify.datamodel.Song
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "PlayerViewModel"
private const val POSITION_POLL_INTERVAL_MS = 500L

/**
 * Wraps the app-scoped [ExoPlayer] instance and exposes its playback state
 * as a [StateFlow]. Scoped to the hosting Activity (see [PlayerModule]) so
 * every screen observes and controls the same player.
 */
@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val exoPlayer: ExoPlayer
) : ViewModel(), Player.Listener {
    private val _uiState = MutableStateFlow(PlayerUiState())
    val uiState: StateFlow<PlayerUiState> = _uiState.asStateFlow()

    init {
        exoPlayer.addListener(this)
        viewModelScope.launch {
            while (isActive) {
                if (exoPlayer.isPlaying) {
                    _uiState.value = _uiState.value.copy(
                        currentMs = exoPlayer.currentPosition.coerceAtLeast(0),
                        durationMs = exoPlayer.duration.coerceAtLeast(0)
                    )
                }
                delay(POSITION_POLL_INTERVAL_MS)
            }
        }
    }

    fun load(song: Song, album: Album) {
        _uiState.value = PlayerUiState(album = album, song = song)
        exoPlayer.setMediaItem(MediaItem.fromUri(song.src))
        exoPlayer.prepare()
    }

    fun play() = exoPlayer.play()

    fun pause() = exoPlayer.pause()

    fun seekTo(positionMs: Long) {
        val safePosition = positionMs.coerceAtLeast(0)
        _uiState.value = _uiState.value.copy(currentMs = safePosition)
        exoPlayer.seekTo(safePosition)
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        _uiState.value = _uiState.value.copy(isPlaying = isPlaying)
    }

    override fun onPlayerError(error: PlaybackException) {
        Log.e(TAG, "Playback error", error)
        _uiState.value = _uiState.value.copy(
            isPlaying = false,
            errorMessage = "Unable to play this track."
        )
    }

    override fun onCleared() {
        exoPlayer.removeListener(this)
        exoPlayer.release()
        super.onCleared()
    }
}

data class PlayerUiState(
    val album: Album? = null,
    val song: Song? = null,
    val isPlaying: Boolean = false,
    val currentMs: Long = 0,
    val durationMs: Long = 0,
    val errorMessage: String? = null,
)
