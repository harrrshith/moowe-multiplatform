package com.harrrshith.moowe.ui.discover

sealed class DiscoverUiEvent {
    data class ShowError(val message: String) : DiscoverUiEvent()
    data class NavigateToMovieDetail(val movieId: Int) : DiscoverUiEvent()
}