package com.example.spotify.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint

/** Displays the user's favorited albums and navigates to their playlist on tap. */
@AndroidEntryPoint
class FavoriteFragment : Fragment() {
    private val viewModel: FavoriteViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme(colors = darkColors()) {
                    FavoriteScreen(viewModel, onTap = { album ->
                        val direction =
                            FavoriteFragmentDirections.actionFavoriteFragmentToPlaylistFragment(album)
                        findNavController().navigate(direction)
                    })
                }
            }
        }
    }
}
