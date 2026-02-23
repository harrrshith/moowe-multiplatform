package com.harrrshith.moowe.ui.discover

import com.harrrshith.moowe.domain.model.Genre
import com.harrrshith.moowe.domain.model.MediaType
import com.harrrshith.moowe.domain.model.Movie


data class DiscoverUiState(
    val selectedMediaType: MediaType = MediaType.MOVIE,
    val trendingMovies: List<Movie> = emptyList(),
    val isTrendingLoading: Boolean = true,
    val genreOrder: List<Genre> = Genre.DiscoverFeedGenres,
    val genreMovies: Map<Genre, List<Movie>> = emptyMap(),
    val loadingGenres: Set<Genre> = emptySet(),
    val loadedGenres: Set<Genre> = emptySet(),
    val errorMessage: String? = null,
    val isRefreshing: Boolean = false
)
