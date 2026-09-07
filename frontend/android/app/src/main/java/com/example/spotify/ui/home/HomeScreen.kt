package com.example.spotify.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.spotify.util.AlbumAsyncImage
import com.example.spotify.R
import com.example.spotify.datamodel.Album
import com.example.spotify.datamodel.Section

/** Home feed: a scrollable list of album sections, or a loading/error state. */
@Composable
fun HomeScreen(viewModel: HomeViewModel, onTap: (Album) -> Unit) {
    val uiState by viewModel.uiState.collectAsState()

    HomeScreenContent(uiState = uiState, onTap = onTap, onRetry = viewModel::fetchHomeScreen)
}

@Composable
fun HomeScreenContent(uiState: HomeUiState, onTap: (Album) -> Unit, onRetry: () -> Unit = {}) {
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        item {
            HomeScreenHeader()
        }

        when {
            uiState.isLoading -> {
                item {
                    LoadingSection(stringResource(id = R.string.screen_loading))
                }
            }
            uiState.errorMessage != null -> {
                item {
                    ErrorSection(uiState.errorMessage, onRetry)
                }
            }
            else -> {
                items(uiState.feed) { item ->
                    AlbumSection(section = item, onTap = onTap)
                }
            }
        }


    }
}

@Composable
private fun AlbumSection(section: Section, onTap: (Album) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = section.sectionTitle,
            style = MaterialTheme.typography.h5.copy(fontWeight =
            FontWeight.Bold),
            color = Color.White
        )
        LazyRow(
            modifier = Modifier.padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(section.albums) { item ->
                AlbumCover(item, onTap)
            }
        }

    }
}

@Composable
private fun AlbumCover(album: Album, onTap: (Album) -> Unit) {
    Column(modifier = Modifier.clickable { onTap(album) }) {
        Box(modifier = Modifier.size(160.dp)) {
            AlbumAsyncImage(
                model = album.cover,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )
            Text(
                text = album.name,
                color = Color.White,
                modifier = Modifier
                    .padding(bottom = 4.dp, start = 2.dp)
                    .align(Alignment.BottomStart),
            )
        }

        Text(
            text = album.artists,
            modifier = Modifier.padding(top = 4.dp),
            style = MaterialTheme.typography.body2.copy(fontWeight =
            FontWeight.Bold),
            color = Color.LightGray,
        )
    }
}

@Composable
private fun LoadingSection(text: String) {
    Row(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = text,
            style = MaterialTheme.typography.body2,
            color = Color.White
        )
    }
}

@Composable
private fun ErrorSection(message: String, onRetry: () -> Unit) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = message,
            style = MaterialTheme.typography.body2,
            color = Color.LightGray
        )
        Text(
            text = stringResource(id = R.string.action_retry),
            modifier = Modifier
                .padding(top = 8.dp)
                .clickable { onRetry() },
            style = MaterialTheme.typography.body2,
            color = Color.Green
        )
    }
}

@Composable
fun HomeScreenHeader() {
    Column {
        Text(
            stringResource(id = R.string.menu_home),
            style = MaterialTheme.typography.h4,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}
