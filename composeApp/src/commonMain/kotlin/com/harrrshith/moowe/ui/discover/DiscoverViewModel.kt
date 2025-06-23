package com.harrrshith.moowe.ui.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harrrshith.moowe.domain.repository.MovieRepository
import com.harrrshith.moowe.domain.utility.Result
import com.harrrshith.moowe.ui.discover.DiscoverUiEvent.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DiscoverViewModel(
    private val mooweRepository: MovieRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(DiscoverUiState())
    val uiState: StateFlow<DiscoverUiState> = _uiState.asStateFlow()
    private val _uiEvents = MutableSharedFlow<DiscoverUiEvent>()
    val uiEvents: SharedFlow<DiscoverUiEvent> = _uiEvents.asSharedFlow()
    init {
        fetchAllCategories()
    }

    private fun fetchAllCategories() {
        fetchTrendingMovies()
    }

    private fun fetchTrendingMovies() {
        viewModelScope.launch {
            when (val response = mooweRepository.getTrendingMovies()) {
                is Result.Loading -> {
                    _uiState.update { it.copy(trendingLoading = true) }
                }
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            trendingLoading = false,
                            trendingMovies = response.data.subList(0, 10), // Limit to 10 movies
                            errorMessage = null
                        )
                    }
                }

                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            trendingLoading = false,
                            errorMessage = response.message
                        )
                    }
                    _uiEvents.emit(ShowError(message = response.message))
                }
            }
        }
    }
}