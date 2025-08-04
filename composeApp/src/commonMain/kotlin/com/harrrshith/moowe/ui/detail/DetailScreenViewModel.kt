package com.harrrshith.moowe.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.harrrshith.moowe.domain.repository.MovieRepository
import com.harrrshith.moowe.ui.navigation.Destination


class DetailScreenViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: MovieRepository
): ViewModel() {
    private val id = savedStateHandle.toRoute<Destination.Detail>()

    init {

    }
}