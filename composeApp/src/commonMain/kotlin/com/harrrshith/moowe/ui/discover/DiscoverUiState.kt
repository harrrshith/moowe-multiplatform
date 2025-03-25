package com.harrrshith.moowe.ui.discover

import com.harrrshith.moowe.domain.model.Movie

data class DiscoverUiState(
    val isLoading: Boolean = false,
    val movies: List<Movie> = emptyList(),
    val error: String? = null
)