package com.harrrshith.moowe.ui.search

import com.harrrshith.moowe.domain.model.Movie

data class SearchUiState(
    val query: String = "",
    val isLoading: Boolean = false,
    val results: List<Movie> = emptyList(),
    val recentSearches: List<Movie> = emptyList(),
    val error: String? = null,
)
