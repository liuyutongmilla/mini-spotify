package com.example.spotify.util

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest

/**
 * A thin wrapper around Coil's [AsyncImage] that attaches a browser-like
 * User-Agent header to outgoing image requests.
 *
 * Some CDNs (Wikimedia's included) reject the default OkHttp User-Agent
 * with an HTTP 403, so all album artwork is loaded through this helper
 * instead of calling [AsyncImage] directly.
 */
@Composable
fun AlbumAsyncImage(
    model: String?,
    modifier: Modifier = Modifier.fillMaxSize(),
    contentScale: ContentScale = ContentScale.FillBounds
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(model)
            .addHeader("User-Agent", "Mozilla/5.0 (Android Emulator) Chrome/120.0")
            .build(),
        contentDescription = null,
        modifier = modifier,
        contentScale = contentScale
    )
}
