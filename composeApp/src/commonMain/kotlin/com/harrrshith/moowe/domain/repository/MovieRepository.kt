package com.harrrshith.moowe.domain.repository

import com.harrrshith.moowe.domain.model.Movie
import com.harrrshith.moowe.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getTrendingMovies(): Flow<Result<List<Movie>>>
}