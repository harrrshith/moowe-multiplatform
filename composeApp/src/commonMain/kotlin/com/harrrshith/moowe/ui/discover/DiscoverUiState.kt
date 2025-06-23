package com.harrrshith.moowe.ui.discover

import com.harrrshith.moowe.domain.model.Movie


data class DiscoverUiState(
    val trendingMovies: List<Movie> = emptyList(),
    val trendingLoading: Boolean = true,
    val actionMovies: List<Movie> = emptyList(),
    val actionLoading: Boolean = false,
    val adventureMovies: List<Movie> = emptyList(),
    val adventureLoading: Boolean = false,
    val romanceMovies: List<Movie> = emptyList(),
    val romanceLoading: Boolean = false,
    val documentaryMovies: List<Movie> = emptyList(),
    val documentaryLoading: Boolean = false,
    val topRatedMovies: List<Movie> = emptyList(),
    val topRatedLoading: Boolean = false,
    val errorMessage: String? = null
) {
    val isLoading: Boolean
        get() = trendingLoading
}