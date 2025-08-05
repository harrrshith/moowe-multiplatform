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
import kotlinx.coroutines.launch


class DetailScreenViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: MovieRepository
): ViewModel() {
    private val movie = savedStateHandle.toRoute<Destination.Detail>()
    private val _uiState: MutableStateFlow<DetailUiState> = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            when (val movie = repository.getMovieById(id = movie.id)) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        movie = movie.data
                    )
                }
                is Result.Error -> { }
                is Result.Loading -> { }
            }
        }
    }
}