package com.harrrshith.moowe.data.repository

import com.harrrshith.moowe.data.remote.MooweApiHandler
import com.harrrshith.moowe.data.toDomain
import com.harrrshith.moowe.domain.model.Genre
import com.harrrshith.moowe.domain.model.Movie
import com.harrrshith.moowe.domain.repository.MovieRepository
import com.harrrshith.moowe.domain.utility.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MovieRepositoryImpl(
    private val api: MooweApiHandler
) : MovieRepository {
    override suspend fun getTrendingMovies(): Result<List<Movie>> {
        return try {
            val movies = withContext(Dispatchers.Default) {
                val response = api.getTrendingMovies()
                response.movies.map { it.toDomain() }
            }
            Result.Success(movies)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown error occurred", Int.MAX_VALUE)
        }
    }

    override suspend fun getMoviesByGenre(genre: Genre): Result<List<Movie>> {
        return try {
            val movies = withContext(Dispatchers.Default) {
                val response = api.getMoviesByGenre(genreId = genre.id)
                response.movies.map { it.toDomain() }
            }
            Result.Success(movies)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknow error occurred", Int.MAX_VALUE)
        }
    }

}