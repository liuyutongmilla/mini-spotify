package com.example.spotify.ui.playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotify.datamodel.Album
import com.example.spotify.datamodel.Song
import com.example.spotify.repository.FavoriteAlbumRepository
import com.example.spotify.repository.PlaylistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Holds and loads the state backing the album/playlist detail screen. */
@HiltViewModel
class PlaylistViewModel @Inject constructor(
    private val playlistRepository: PlaylistRepository,
    private val favoriteAlbumRepository: FavoriteAlbumRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(PlaylistUiState(album = Album.empty()))
    val uiState: StateFlow<PlaylistUiState> = _uiState.asStateFlow()

    fun fetchPlaylist(album: Album) {
        _uiState.value = _uiState.value.copy(album = album, isLoading = true, errorMessage = null)

        viewModelScope.launch {
            runCatching { playlistRepository.getPlaylist(album.id) }
                .onSuccess { playlist ->
                    _uiState.value = _uiState.value.copy(
                        playlist = playlist.songs,
                        isLoading = false
                    )
                }
                .onFailure {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Unable to load this playlist."
                    )
                }
        }

        viewModelScope.launch {
            favoriteAlbumRepository.isFavoriteAlbum(album.id).collect { isFavorite ->
                _uiState.value = _uiState.value.copy(isFavorite = isFavorite)
            }
        }
    }

    fun toggleFavorite(shouldFavorite: Boolean) {
        val album = _uiState.value.album
        viewModelScope.launch {
            if (shouldFavorite) {
                favoriteAlbumRepository.favoriteAlbum(album)
            } else {
                favoriteAlbumRepository.unFavoriteAlbum(album)
            }
        }
    }
}

data class PlaylistUiState(
    val album: Album,
    val isFavorite: Boolean = false,
    val playlist: List<Song> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
