package com.harrrshith.moowe.domain.repository

import com.harrrshith.moowe.domain.model.Genre
import com.harrrshith.moowe.domain.model.Movie
import com.harrrshith.moowe.domain.utility.Result

interface MovieRepository {
    suspend fun getTrendingMovies(): Result<List<Movie>>

    suspend fun getMoviesByGenre(genre: Genre): Result<List<Movie>>
}