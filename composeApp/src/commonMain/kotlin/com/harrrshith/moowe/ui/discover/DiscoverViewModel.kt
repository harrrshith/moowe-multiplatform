package com.harrrshith.moowe.ui.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harrrshith.moowe.domain.repository.MovieRepository
import kotlinx.coroutines.launch

class DiscoverViewModel(
    mooweRepository: MovieRepository
): ViewModel() {
    init {
        print("Hello world")
        viewModelScope.launch {
            val movies = mooweRepository.getTrendingMovies()
            print(movies)
        }
    }
}