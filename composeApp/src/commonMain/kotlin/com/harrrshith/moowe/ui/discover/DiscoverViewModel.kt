package com.harrrshith.moowe.ui.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harrrshith.moowe.domain.model.Genre
import com.harrrshith.moowe.domain.model.MediaType
import com.harrrshith.moowe.domain.repository.MovieRepository
import com.harrrshith.moowe.domain.utility.Result
import com.harrrshith.moowe.ui.utility.successOrEmpty
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DiscoverViewModel(
    private val repository: MovieRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(DiscoverUiState())
    val uiState: StateFlow<DiscoverUiState> = _uiState.asStateFlow()
    private val _uiEvents = MutableSharedFlow<DiscoverUiEvent>()
    val uiEvents: SharedFlow<DiscoverUiEvent> = _uiEvents.asSharedFlow()

    init {
        fetchAllMedia(MediaType.MOVIE)
    }

    private fun fetchAllMedia(mediaType: MediaType) {
        viewModelScope.launch {
            // Only show loading if we don't have any data yet (first load)
            // This implements local-first: cached data shows immediately without loader
            val shouldShowLoading = _uiState.value.trendingMovies.isNullOrEmpty() &&
                                   _uiState.value.actionMovies.isNullOrEmpty()
            
            if (shouldShowLoading) {
                _uiState.update { it.copy(isLoading = true) }
            }
            
            try {
                combine(
                    repository.getTrendingMedia(mediaType),
                    repository.getMediaByGenre(mediaType, genre = Genre.ACTION),
                    repository.getMediaByGenre(mediaType, genre = Genre.ADVENTURE),
                    repository.getMediaByGenre(mediaType, genre = Genre.FANTASY),
                    repository.getMediaByGenre(mediaType, genre = Genre.DOCUMENTARY)
                ) { trendingMovies, actionMovies, adventureMovies, fantasyMovies, documentaries ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,  // Hide loading once we have data (cached or fresh)
                            selectedMediaType = mediaType,
                            trendingMovies = trendingMovies.successOrEmpty().take(10),
                            actionMovies = actionMovies.successOrEmpty(),
                            adventureMovies = adventureMovies.successOrEmpty(),
                            fantasyMovies = fantasyMovies.successOrEmpty(),
                            documentaries = documentaries.successOrEmpty(),
                            errorMessage = listOfNotNull(
                                (trendingMovies as? Result.Error)?.message,
                                (actionMovies as? Result.Error)?.message,
                                (adventureMovies as? Result.Error)?.message,
                                (fantasyMovies as? Result.Error)?.message,
                                (documentaries as? Result.Error)?.message
                            ).firstOrNull()
                        )
                    }
                }.catch { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = e.message
                        )
                    }
                }.collect()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message
                    )
                }
            }
        }
    }

    fun onMediaTypeChanged(mediaType: MediaType) {
        if (_uiState.value.selectedMediaType != mediaType) {
            fetchAllMedia(mediaType)
        }
    }

    fun onMovieClick(id: Int) {
        viewModelScope.launch {
            _uiEvents.emit(DiscoverUiEvent.NavigateToDetail(id))
        }
    }
}