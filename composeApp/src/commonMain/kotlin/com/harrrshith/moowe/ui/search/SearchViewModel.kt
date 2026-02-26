package com.harrrshith.moowe.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harrrshith.moowe.domain.model.MediaType
import com.harrrshith.moowe.domain.model.Movie
import com.harrrshith.moowe.domain.repository.MovieRepository
import com.harrrshith.moowe.domain.utility.Result
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repository: MovieRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val _uiEvents = MutableSharedFlow<SearchUiEvent>()
    val uiEvents: SharedFlow<SearchUiEvent> = _uiEvents.asSharedFlow()

    private var searchJob: Job? = null

    init {
        viewModelScope.launch {
            repository.getRecentSearches(limit = 10).collect { recent ->
                _uiState.update { it.copy(recentSearches = recent) }
            }
        }
    }

    fun onQueryChange(query: String) {
        _uiState.update { it.copy(query = query) }
        performSearch()
    }

    fun onMediaTypeChanged(mediaType: MediaType) {
        _uiState.update { it.copy(selectedMediaType = mediaType) }
        performSearch()
    }

    fun onMovieClick(movie: Movie) {
        viewModelScope.launch {
            repository.addRecentSearch(movie)
            emitNavigateToDetail(movie)
        }
    }

    fun onRecentMovieClick(movie: Movie) {
        viewModelScope.launch {
            emitNavigateToDetail(movie)
        }
    }

    private suspend fun emitNavigateToDetail(movie: Movie) {
        _uiEvents.emit(
            SearchUiEvent.NavigateToDetail(
                id = movie.id,
                mediaType = movie.mediaType,
                sharedKey = "search-${movie.mediaType.name}-${movie.id}",
                title = movie.title,
                posterPath = movie.posterPath,
            )
        )
    }

    private fun performSearch() {
        val query = _uiState.value.query
        val mediaType = _uiState.value.selectedMediaType

        searchJob?.cancel()

        if (query.isBlank()) {
            _uiState.update { it.copy(results = emptyList(), isLoading = false, error = null) }
            return
        }

        searchJob = viewModelScope.launch {
            delay(350)
            _uiState.update { it.copy(isLoading = true, error = null) }

            when (val result = repository.searchMedia(query = query.trim(), mediaType = mediaType)) {
                is Result.Success -> _uiState.update {
                    it.copy(
                        isLoading = false,
                        results = result.data,
                    )
                }

                is Result.Error -> _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = result.message,
                        results = emptyList(),
                    )
                }

                is Result.Loading -> Unit
            }
        }
    }
}
