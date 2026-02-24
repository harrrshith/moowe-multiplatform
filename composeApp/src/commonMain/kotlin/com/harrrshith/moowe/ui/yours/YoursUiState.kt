package com.harrrshith.moowe.ui.yours

import com.harrrshith.moowe.domain.model.Movie

data class FavoriteSection(
    val header: String,
    val movies: List<Movie>,
)

data class YoursUiState(
    val sections: List<FavoriteSection> = emptyList(),
)
