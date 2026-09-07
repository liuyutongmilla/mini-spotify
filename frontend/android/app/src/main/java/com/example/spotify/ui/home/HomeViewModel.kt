package com.example.spotify.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotify.datamodel.Section
import com.example.spotify.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Holds and loads the state backing the home feed screen. */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HomeRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState(isLoading = true))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        fetchHomeScreen()
    }

    fun fetchHomeScreen() {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            runCatching { repository.getHomeSections() }
                .onSuccess { sections ->
                    _uiState.value = HomeUiState(feed = sections)
                }
                .onFailure {
                    _uiState.value = HomeUiState(errorMessage = "Unable to load the home feed.")
                }
        }
    }
}

data class HomeUiState(
    val feed: List<Section> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
