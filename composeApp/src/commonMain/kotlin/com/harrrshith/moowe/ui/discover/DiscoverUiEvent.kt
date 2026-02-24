package com.harrrshith.moowe.ui.discover

import com.harrrshith.moowe.domain.model.MediaType

sealed class DiscoverUiEvent {
    data class ShowError(val message: String) : DiscoverUiEvent()
    data class NavigateToDetail(
        val id: Int,
        val mediaType: MediaType,
        val sharedKey: String,
        val title: String,
        val posterPath: String,
    ) : DiscoverUiEvent()
}
