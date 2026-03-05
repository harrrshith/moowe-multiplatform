package com.harrrshith.moowe.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harrrshith.moowe.domain.model.CastMember
import com.harrrshith.moowe.domain.model.MediaType
import com.harrrshith.moowe.domain.repository.MovieRepository
import com.harrrshith.moowe.domain.utility.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class DetailScreenViewModel(
    private val movieId: Int,
    private val mediaType: MediaType,
    private val repository: MovieRepository
): ViewModel() {
    private val _uiState: MutableStateFlow<DetailUiState> = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.isFavorite(movieId = movieId, mediaType = mediaType).collect { favorite ->
                _uiState.update { it.copy(isLiked = favorite) }
            }
        }

        viewModelScope.launch {
            when (val result = repository.getMediaById(id = movieId, mediaType = mediaType)) {
                is Result.Success -> _uiState.update { it.copy(movie = result.data) }
                is Result.Error -> _uiState.update { it.copy(error = result.message) }
                is Result.Loading -> { }
            }
        }
        viewModelScope.launch {
            when (val result = repository.getMediaReviews(mediaId = movieId, mediaType = mediaType)) {
                is Result.Success -> _uiState.update { it.copy(reviews = result.data) }
                is Result.Error -> { }
                is Result.Loading -> { }
            }
        }

        viewModelScope.launch {
            when (val result = repository.getMediaCast(mediaId = movieId, mediaType = mediaType)) {
                is Result.Success -> _uiState.update { it.copy(cast = result.data) }
                is Result.Error -> { }
                is Result.Loading -> { }
            }
        }

        viewModelScope.launch {
            when (val result = repository.getRelatedMedia(mediaId = movieId, mediaType = mediaType)) {
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

    fun onCastClicked(member: CastMember) {
        viewModelScope.launch {
            val currentState = _uiState.value
            if (currentState.selectedCastMember?.id == member.id) {
                _uiState.update {
                    it.copy(
                        selectedCastMember = null,
                        selectedCastCredits = emptyList(),
                        isCastCreditsLoading = false,
                    )
                }
                return@launch
            }

            _uiState.update {
                it.copy(
                    selectedCastMember = member,
                    selectedCastCredits = emptyList(),
                    isCastCreditsLoading = true,
                )
            }

            when (val result = repository.getPersonCredits(personId = member.id)) {
                is Result.Success -> _uiState.update {
                    it.copy(
                        selectedCastCredits = result.data,
                        isCastCreditsLoading = false,
                    )
                }
                is Result.Error -> _uiState.update {
                    it.copy(
                        selectedCastCredits = emptyList(),
                        isCastCreditsLoading = false,
                    )
                }
                is Result.Loading -> { }
            }
        }
    }
}
