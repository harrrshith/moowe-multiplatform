package com.harrrshith.moowe.ui.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harrrshith.moowe.domain.model.Genre
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
        fetchAllMovies()
    }

    private fun fetchAllMovies() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                combine(
                    repository.getTrendingMovies(),
                    repository.getMoviesByGenre(genre = Genre.ACTION),
                    repository.getMoviesByGenre(genre = Genre.ADVENTURE),
                    repository.getMoviesByGenre(genre = Genre.FANTASY),
                    repository.getMoviesByGenre(genre = Genre.DOCUMENTARY)
                ) { trendingMovies, actionMovies, adventureMovies, fantasyMovies, documentaries ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
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

    fun onMovieClick(id: Int) {
        viewModelScope.launch {
            _uiEvents.emit(DiscoverUiEvent.NavigateToDetail(id))
        }
    }
}