package com.harrrshith.moowe.ui.discover

sealed class DiscoverUiEvent {
    data class ShowError(val message: String) : DiscoverUiEvent()
    data class NavigateToDetail(val id: Int) : DiscoverUiEvent()
}