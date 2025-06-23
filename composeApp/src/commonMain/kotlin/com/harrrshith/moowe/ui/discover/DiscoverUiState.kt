package com.harrrshith.moowe.ui.discover

import com.harrrshith.moowe.domain.model.Movie


data class DiscoverUiState(
    val trendingMovies: List<Movie> = emptyList(),
    val trendingLoading: Boolean = true,
    val actionMovies: List<Movie> = emptyList(),
    val actionLoading: Boolean = true,
    val adventureMovies: List<Movie> = emptyList(),
    val adventureLoading: Boolean = true,
    val romanceMovies: List<Movie> = emptyList(),
    val romanceLoading: Boolean = true,
    val documentaryMovies: List<Movie> = emptyList(),
    val documentaryLoading: Boolean = true,
    val topRatedMovies: List<Movie> = emptyList(),
    val topRatedLoading: Boolean = true,
    val errorMessage: String? = null
) {
    val isLoading: Boolean
        get() = trendingLoading || actionLoading || adventureLoading ||
                romanceLoading || documentaryLoading || topRatedLoading
}