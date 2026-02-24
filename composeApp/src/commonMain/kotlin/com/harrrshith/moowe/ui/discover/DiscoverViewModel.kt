package com.harrrshith.moowe.ui.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harrrshith.moowe.domain.model.Genre
import com.harrrshith.moowe.domain.model.MediaType
import com.harrrshith.moowe.domain.repository.MovieRepository
import com.harrrshith.moowe.domain.utility.Result
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DiscoverViewModel(
    private val repository: MovieRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(DiscoverUiState())
    val uiState: StateFlow<DiscoverUiState> = _uiState.asStateFlow()

    private val _uiEvents = MutableSharedFlow<DiscoverUiEvent>()
    val uiEvents: SharedFlow<DiscoverUiEvent> = _uiEvents.asSharedFlow()

    private var trendingJob: Job? = null
    private val genreJobs: MutableMap<Genre, Job> = mutableMapOf()
    
    var selectedMediaType: MediaType = MediaType.MOVIE

    init {
        loadFeed(selectedMediaType, forceRefresh = false)
    }

    private fun loadFeed(mediaType: MediaType, forceRefresh: Boolean) {
        cancelAllFeedJobs()

        _uiState.update {
            it.copy(
                selectedMediaType = mediaType,
                trendingMovies = emptyList(),
                isTrendingLoading = true,
                genreMovies = emptyMap(),
                loadingGenres = emptySet(),
                loadedGenres = emptySet(),
                errorMessage = null,
                isRefreshing = forceRefresh,
            )
        }

        loadTrending(forceRefresh = forceRefresh)

        _uiState.value.genreOrder.forEach { genre ->
            loadGenre(genre = genre, forceRefresh = forceRefresh)
        }
    }

    private fun loadTrending(forceRefresh: Boolean) {
        trendingJob?.cancel()
        trendingJob = viewModelScope.launch {
            repository.getTrendingMedia(
                mediaType = selectedMediaType,
                forceRefresh = forceRefresh,
            ).collect { result ->
                when (result) {
                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                trendingMovies = result.data.take(12),
                                isTrendingLoading = false,
                                isRefreshing = false,
                            )
                        }
                    }

                    is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                isTrendingLoading = false,
                                isRefreshing = false,
                                errorMessage = result.message,
                            )
                        }
                    }

                    is Result.Loading -> {
                        _uiState.update { it.copy(isTrendingLoading = true) }
                    }
                }
            }
        }
    }

    private fun loadGenre(genre: Genre, forceRefresh: Boolean = false) {
        val state = _uiState.value
        if (genre !in state.genreOrder) return
        if (!forceRefresh && (genre in state.loadingGenres || genre in state.loadedGenres)) return

        genreJobs[genre]?.cancel()
        _uiState.update { it.copy(loadingGenres = it.loadingGenres + genre) }

        genreJobs[genre] = viewModelScope.launch {
            repository.getMediaByGenre(
                mediaType = selectedMediaType,
                genre = genre,
                forceRefresh = forceRefresh,
            ).collect { result ->
                when (result) {
                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                genreMovies = it.genreMovies + (genre to result.data),
                                loadingGenres = it.loadingGenres - genre,
                                loadedGenres = it.loadedGenres + genre,
                                isRefreshing = false,
                            )
                        }
                    }

                    is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                loadingGenres = it.loadingGenres - genre,
                                loadedGenres = it.loadedGenres + genre,
                                errorMessage = result.message,
                                isRefreshing = false,
                            )
                        }
                    }

                    is Result.Loading -> {
                        _uiState.update { ui ->
                            if (genre in ui.loadingGenres) ui else ui.copy(loadingGenres = ui.loadingGenres + genre)
                        }
                    }
                }
            }
        }
    }

    private fun cancelAllFeedJobs() {
        trendingJob?.cancel()
        trendingJob = null
        genreJobs.values.forEach { it.cancel() }
        genreJobs.clear()
    }

    fun onMediaTypeChanged(mediaType: MediaType) {
        if (_uiState.value.selectedMediaType != mediaType) {
            selectedMediaType = mediaType
            loadFeed(mediaType, forceRefresh = false)
        }
    }

    fun onRefresh() {
        loadFeed(selectedMediaType, forceRefresh = true)
    }

    fun onGenreVisible(genre: Genre) {
        loadGenre(genre = genre)
    }

    fun onMovieClick(
        id: Int,
        mediaType: MediaType,
        sharedKey: String,
        title: String,
        posterPath: String,
    ) {
        viewModelScope.launch {
            _uiEvents.emit(
                DiscoverUiEvent.NavigateToDetail(
                    id = id,
                    mediaType = mediaType,
                    sharedKey = sharedKey,
                    title = title,
                    posterPath = posterPath,
                )
            )
        }
    }
}
