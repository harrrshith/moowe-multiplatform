package com.harrrshith.moowe.ui.trending

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.harrrshith.moowe.domain.model.Genre
import com.harrrshith.moowe.domain.model.MediaType
import com.harrrshith.moowe.domain.model.Movie
import com.harrrshith.moowe.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update

class TrendingViewModel(
    private val repository: MovieRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(TrendingUiState())
    val uiState: StateFlow<TrendingUiState> = _uiState.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val pagedMovies: Flow<PagingData<Movie>> =
        uiState
            .flatMapLatest { state ->
                repository.getTrendingPagedMedia(
                    mediaType = state.selectedMediaType,
                    genre = state.selectedGenre,
                    pageSize = 20,
                )
            }
            .cachedIn(viewModelScope)

    fun onMediaTypeChanged(mediaType: MediaType) {
        if (_uiState.value.selectedMediaType == mediaType) return
        _uiState.update { it.copy(selectedMediaType = mediaType) }
    }

    fun onGenreSelected(genre: Genre) {
        if (_uiState.value.selectedGenre == genre) return
        _uiState.update { it.copy(selectedGenre = genre) }
    }
}
