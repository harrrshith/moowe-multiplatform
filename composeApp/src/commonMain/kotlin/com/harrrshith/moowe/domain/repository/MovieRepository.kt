package com.harrrshith.moowe.domain.repository

import com.harrrshith.moowe.domain.model.Movie
import com.harrrshith.moowe.domain.utility.Result

interface MovieRepository {
    suspend fun getTrendingMovies(): Result<List<Movie>>
}