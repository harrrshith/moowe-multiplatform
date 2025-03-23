package com.harrrshith.moowe.ui.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harrrshith.moowe.domain.repository.MovieRepository
import com.harrrshith.moowe.domain.util.Result.Error
import com.harrrshith.moowe.domain.util.Result.Loading
import com.harrrshith.moowe.domain.util.Result.Success
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DiscoverViewModel(
    private val movieRepository: MovieRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(DiscoverUiState())
    val uiState: StateFlow<DiscoverUiState> = _uiState.asStateFlow()
    private val _uiEvents = MutableSharedFlow<DiscoverUiEvent>()
    val uiEvents: SharedFlow<DiscoverUiEvent> = _uiEvents.asSharedFlow()

    init {
        fetchTrendingMovies()
    }
    fun fetchTrendingMovies() {
        viewModelScope.launch {
            movieRepository.getTrendingMovies().collect { result ->
                when (result) {
                    is Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                movies = result.data,
                                error = null
                            )
                        }
                    }
                    is Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = result.message
                            )
                        }
                        _uiEvents.emit(DiscoverUiEvent.ShowError(result.message))
                    }
                }
            }
        }
    }

}