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
                coroutineScope {
                    val trendingDeferred = async { repository.getTrendingMovies() }
                    val actionDeferred = async { repository.getMoviesByGenre(Genre.ACTION) }
                    val adventureDeferred = async { repository.getMoviesByGenre(Genre.ADVENTURE) }
                    val fantasyDeferred = async { repository.getMoviesByGenre(Genre.FANTASY) }
                    val documentaryDeferred = async { repository.getMoviesByGenre(Genre.DOCUMENTARY) }

                    val trendingResult = trendingDeferred.await()
                    val actionResult = actionDeferred.await()
                    val adventureResult = adventureDeferred.await()
                    val fantasyResult = fantasyDeferred.await()
                    val documentaryResult = documentaryDeferred.await()
                    print("Hello world\n\n $trendingResult")
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            trendingMovies = trendingResult.successOrEmpty().take(10),
                            actionMovies = actionResult.successOrEmpty(),
                            adventureMovies = adventureResult.successOrEmpty(),
                            fantasyMovies = fantasyResult.successOrEmpty(),
                            documentaries = documentaryResult.successOrEmpty(),
                            errorMessage = listOfNotNull(
                                (trendingResult as? Result.Error)?.message,
                                (actionResult as? Result.Error)?.message,
                                (adventureResult as? Result.Error)?.message,
                                (fantasyResult as? Result.Error)?.message,
                                (documentaryResult as? Result.Error)?.message
                            ).firstOrNull()
                        )
                    }
                }
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
}