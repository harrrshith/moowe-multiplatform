package com.harrrshith.moowe.ui.discover

import com.harrrshith.moowe.domain.model.MediaType
import com.harrrshith.moowe.domain.model.Movie


data class DiscoverUiState(
    val selectedMediaType: MediaType = MediaType.MOVIE,
    val trendingMovies: List<Movie> = emptyList(),
    val actionMovies: List<Movie> = emptyList(),
    val adventureMovies: List<Movie> = emptyList(),
    val fantasyMovies: List<Movie> = emptyList(),
    val documentaries: List<Movie> = emptyList(),
    val topRatedMovies: List<Movie> = emptyList(),
    val errorMessage: String? = null,
    val isLoading: Boolean = false
)