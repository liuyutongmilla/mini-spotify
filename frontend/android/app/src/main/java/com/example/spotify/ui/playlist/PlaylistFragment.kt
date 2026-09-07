package com.example.spotify.ui.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.spotify.player.PlayerViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Displays an album's detail view and song list. Shares a single
 * [PlayerViewModel] (scoped to the hosting Activity) with the rest of the
 * app so playback state and the bottom player bar stay in sync across
 * navigation.
 */
@AndroidEntryPoint
class PlaylistFragment : Fragment() {
    private val navArgs by navArgs<PlaylistFragmentArgs>()
    private val viewModel: PlaylistViewModel by viewModels()
    private val playerViewModel: PlayerViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme(colors = darkColors()) {
                    PlaylistScreen(
                        playlistViewModel = viewModel,
                        playerViewModel = playerViewModel
                    )
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchPlaylist(navArgs.album)
    }
}
