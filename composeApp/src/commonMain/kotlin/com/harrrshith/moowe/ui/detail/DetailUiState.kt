package com.harrrshith.moowe.ui.detail

import com.harrrshith.moowe.domain.model.Movie
import com.harrrshith.moowe.domain.model.Review

data class DetailUiState(
    val movie: Movie? = null,
    val reviews: List<Review> = emptyList(),
    val isLiked: Boolean = false,
    val isBookmarked: Boolean = false,
    val error: String? = null,
)