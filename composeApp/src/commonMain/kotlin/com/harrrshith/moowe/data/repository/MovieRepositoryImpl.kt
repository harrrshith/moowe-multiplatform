package com.harrrshith.moowe.data.repository

import com.harrrshith.moowe.data.remote.MooweApiHandler
import com.harrrshith.moowe.data.toDomain
import com.harrrshith.moowe.domain.model.Movie
import com.harrrshith.moowe.domain.repository.MovieRepository
import com.harrrshith.moowe.domain.utility.Result

class MovieRepositoryImpl(
    private val api: MooweApiHandler
) : MovieRepository {
    override suspend fun getTrendingMovies(): Result<List<Movie>> {
        return try {
            val response = api.getTrendingMovies()
            val movies = response.movies.map { it.toDomain() }
            Result.Success(movies)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown error occurred", Int.MAX_VALUE)
        }
    }

}