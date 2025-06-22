package com.harrrshith.moowe.ui.discover

import androidx.lifecycle.ViewModel
import com.harrrshith.moowe.domain.model.Movie
import com.harrrshith.moowe.domain.repository.MovieRepository
import com.harrrshith.moowe.domain.utility.Result

class DiscoverViewModel(
    private val mooweRepository: MovieRepository
): ViewModel() {
    val movie: List<Movie> = emptyList()
    suspend fun fetchTrendingMovies() {
        when (val trendingMovies = mooweRepository.getTrendingMovies()) {
            is Result.Error -> print("Error")
            Result.Loading -> print("Loading")
            is Result.Success -> print(trendingMovies.data)
        }

    }
}