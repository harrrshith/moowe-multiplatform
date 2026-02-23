package com.harrrshith.moowe.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.harrrshith.moowe.domain.repository.MovieRepository
import com.harrrshith.moowe.domain.utility.Result
import com.harrrshith.moowe.ui.navigation.Destination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class DetailScreenViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: MovieRepository
): ViewModel() {
    private val dest = savedStateHandle.toRoute<Destination.Detail>()
    private val _uiState: MutableStateFlow<DetailUiState> = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            when (val result = repository.getMovieById(id = dest.id)) {
                is Result.Success -> _uiState.update { it.copy(movie = result.data) }
                is Result.Error -> _uiState.update { it.copy(error = result.message) }
                is Result.Loading -> { }
            }
        }
        viewModelScope.launch {
            when (val result = repository.getMovieReviews(movieId = dest.id)) {
                is Result.Success -> _uiState.update { it.copy(reviews = result.data) }
                is Result.Error -> { }
                is Result.Loading -> { }
            }
        }

        viewModelScope.launch {
            when (val result = repository.getMovieCast(movieId = dest.id)) {
                is Result.Success -> _uiState.update { it.copy(cast = result.data) }
                is Result.Error -> { }
                is Result.Loading -> { }
            }
        }

        viewModelScope.launch {
            when (val result = repository.getRelatedMovies(movieId = dest.id)) {
                is Result.Success -> _uiState.update { it.copy(relatedMovies = result.data) }
                is Result.Error -> { }
                is Result.Loading -> { }
            }
        }
    }
}
