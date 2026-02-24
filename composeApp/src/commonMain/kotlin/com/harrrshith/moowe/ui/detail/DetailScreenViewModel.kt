package com.harrrshith.moowe.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.harrrshith.moowe.domain.model.MediaType
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
    private val mediaType = MediaType.fromApiValue(dest.mediaType)
    private val _uiState: MutableStateFlow<DetailUiState> = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.isFavorite(movieId = dest.id, mediaType = mediaType).collect { favorite ->
                _uiState.update { it.copy(isLiked = favorite) }
            }
        }

        viewModelScope.launch {
            when (val result = repository.getMediaById(id = dest.id, mediaType = mediaType)) {
                is Result.Success -> _uiState.update { it.copy(movie = result.data) }
                is Result.Error -> _uiState.update { it.copy(error = result.message) }
                is Result.Loading -> { }
            }
        }
        viewModelScope.launch {
            when (val result = repository.getMediaReviews(mediaId = dest.id, mediaType = mediaType)) {
                is Result.Success -> _uiState.update { it.copy(reviews = result.data) }
                is Result.Error -> { }
                is Result.Loading -> { }
            }
        }

        viewModelScope.launch {
            when (val result = repository.getMediaCast(mediaId = dest.id, mediaType = mediaType)) {
                is Result.Success -> _uiState.update { it.copy(cast = result.data) }
                is Result.Error -> { }
                is Result.Loading -> { }
            }
        }

        viewModelScope.launch {
            when (val result = repository.getRelatedMedia(mediaId = dest.id, mediaType = mediaType)) {
                is Result.Success -> _uiState.update { it.copy(relatedMovies = result.data) }
                is Result.Error -> { }
                is Result.Loading -> { }
            }
        }
    }

    fun onLikeClicked() {
        viewModelScope.launch {
            val movie = _uiState.value.movie ?: return@launch
            if (_uiState.value.isLiked) {
                repository.removeFavorite(movieId = movie.id, mediaType = movie.mediaType)
            } else {
                repository.addFavorite(movie)
            }
        }
    }
}
