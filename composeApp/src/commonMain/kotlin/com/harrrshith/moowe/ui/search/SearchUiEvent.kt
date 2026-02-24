package com.harrrshith.moowe.ui.search

import com.harrrshith.moowe.domain.model.MediaType

sealed class SearchUiEvent {
    data class NavigateToDetail(
        val id: Int,
        val mediaType: MediaType,
        val sharedKey: String,
        val title: String,
        val posterPath: String,
    ) : SearchUiEvent()
}
