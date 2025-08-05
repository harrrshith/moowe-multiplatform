package com.harrrshith.moowe.ui.detail

import com.harrrshith.moowe.domain.model.Movie

data class DetailUiState(
    val movie: Movie? = null,
    val isLiked: Boolean = false,
    val isBookmarked: Boolean = false,
    val error: String? = null
)