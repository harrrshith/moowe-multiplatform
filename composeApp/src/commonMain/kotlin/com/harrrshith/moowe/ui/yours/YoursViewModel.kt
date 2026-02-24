package com.harrrshith.moowe.ui.yours

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harrrshith.moowe.domain.model.Genre
import com.harrrshith.moowe.domain.model.MediaType
import com.harrrshith.moowe.domain.model.Movie
import com.harrrshith.moowe.domain.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class YoursViewModel(
    private val repository: MovieRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(YoursUiState())
    val uiState: StateFlow<YoursUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getFavorites().collect { favorites ->
                val sections = favorites
                    .groupBy { movie -> movie.resolveHeader() }
                    .map { (header, movies) -> FavoriteSection(header = header, movies = movies) }
                _uiState.update { it.copy(sections = sections) }
            }
        }
    }

    private fun Movie.resolveHeader(): String {
        val primaryGenreId = genreIds.firstOrNull() ?: return "Others"
        val genre = Genre.entries.firstOrNull { entry ->
            when (mediaType) {
                MediaType.MOVIE -> entry.id == primaryGenreId
                MediaType.TV_SERIES -> entry.tvId == primaryGenreId || entry.id == primaryGenreId
            }
        }
        return genre?.displayName ?: "Others"
    }
}
