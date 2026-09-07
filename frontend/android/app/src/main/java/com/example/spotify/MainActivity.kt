package com.example.spotify

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.ui.platform.ComposeView
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.spotify.player.PlayerBar
import com.example.spotify.player.PlayerViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Application entry point. Hosts the bottom navigation graph (Home / Favorite /
 * Playlist) and a persistent player bar that stays mounted across all
 * destinations so playback state survives navigation.
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val playerViewModel: PlayerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpNavigation()
        setUpPlayerBar()
    }

    private fun setUpNavigation() {
        val navView = findViewById<BottomNavigationView>(R.id.nav_view)
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        navController.setGraph(R.navigation.nav_graph)

        NavigationUI.setupWithNavController(navView, navController)

        // NavigationUI's default listener does not pop back to the graph's
        // start destination when re-selecting a bottom nav item, so the
        // back stack is trimmed explicitly here.
        navView.setOnItemSelectedListener {
            NavigationUI.onNavDestinationSelected(it, navController)
            navController.popBackStack(it.itemId, inclusive = false)
            true
        }
    }

    private fun setUpPlayerBar() {
        findViewById<ComposeView>(R.id.player_bar).apply {
            setContent {
                MaterialTheme(colors = darkColors()) {
                    PlayerBar(playerViewModel)
                }
            }
        }
    }
}
