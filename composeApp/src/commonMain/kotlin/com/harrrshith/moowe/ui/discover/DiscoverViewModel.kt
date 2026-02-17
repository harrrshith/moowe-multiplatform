package com.harrrshith.moowe.ui.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harrrshith.moowe.domain.model.Genre
import com.harrrshith.moowe.domain.model.MediaType
import com.harrrshith.moowe.domain.repository.MovieRepository
import com.harrrshith.moowe.domain.utility.Result
import com.harrrshith.moowe.ui.utility.successOrEmpty
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

    private fun fetchAllMedia(mediaType: MediaType, forceRefresh: Boolean = false) {
        viewModelScope.launch {
            // Show full-screen loader only on first ever load (no cached data at all)
            val shouldShowLoading = !forceRefresh &&
                                   _uiState.value.trendingMovies.isEmpty() &&
                                   _uiState.value.actionMovies.isEmpty()

            _uiState.update {
                it.copy(
                    isLoading = shouldShowLoading,
                    isRefreshing = forceRefresh
                )
            }

            try {
                combine(
                    repository.getTrendingMedia(mediaType, forceRefresh),
                    repository.getMediaByGenre(mediaType, genre = Genre.ACTION, forceRefresh),
                    repository.getMediaByGenre(mediaType, genre = Genre.ADVENTURE, forceRefresh),
                    repository.getMediaByGenre(mediaType, genre = Genre.FANTASY, forceRefresh),
                    repository.getMediaByGenre(mediaType, genre = Genre.DOCUMENTARY, forceRefresh)
                ) { trendingMovies, actionMovies, adventureMovies, fantasyMovies, documentaries ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isRefreshing = false,
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
                            isRefreshing = false,
                            errorMessage = e.message
                        )
                    }
                }.collect()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isRefreshing = false,
                        errorMessage = e.message
                    )
                }
            }
        }
    }

    fun onMediaTypeChanged(mediaType: MediaType) {
        if (_uiState.value.selectedMediaType != mediaType) {
            fetchAllMedia(mediaType, forceRefresh = false)
        }
    }

    fun onRefresh() {
        fetchAllMedia(_uiState.value.selectedMediaType, forceRefresh = true)
    }

    fun onMovieClick(id: Int) {
        viewModelScope.launch {
            _uiEvents.emit(DiscoverUiEvent.NavigateToDetail(id))
        }
    }
}