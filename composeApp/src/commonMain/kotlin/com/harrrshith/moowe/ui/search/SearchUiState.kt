package com.harrrshith.moowe.ui.search

import com.harrrshith.moowe.domain.model.MediaType
import com.harrrshith.moowe.domain.model.Movie

data class SearchUiState(
    val query: String = "",
    val selectedMediaType: MediaType = MediaType.MOVIE,
    val isLoading: Boolean = false,
    val results: List<Movie> = emptyList(),
    val recentSearches: List<Movie> = emptyList(),
    val error: String? = null,
)
