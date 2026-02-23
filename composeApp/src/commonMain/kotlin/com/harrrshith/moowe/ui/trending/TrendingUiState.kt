package com.harrrshith.moowe.ui.trending

import com.harrrshith.moowe.domain.model.Genre
import com.harrrshith.moowe.domain.model.MediaType

data class TrendingUiState(
    val selectedMediaType: MediaType = MediaType.MOVIE,
    val selectedGenre: Genre = Genre.TRENDING,
    val genres: List<Genre> = listOf(Genre.TRENDING) + Genre.DiscoverFeedGenres.take(10),
)
