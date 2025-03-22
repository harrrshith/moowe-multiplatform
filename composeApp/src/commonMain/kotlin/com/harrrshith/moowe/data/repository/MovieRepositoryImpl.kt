package com.harrrshith.moowe.data.repository

import com.harrrshith.moowe.data.mapper.toDomain
import com.harrrshith.moowe.data.remote.MovieApi
import com.harrrshith.moowe.domain.model.Movie
import com.harrrshith.moowe.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.harrrshith.moowe.domain.util.Result


class MovieRepositoryImpl(
    private val api: MovieApi
) : MovieRepository {

    override fun getTrendingMovies(): Flow<Result<List<Movie>>> = flow {
        emit(Result.Loading)
        try {
            val response = api.getTrendingMovies()
            val movies = response.movies.map { it.toDomain() }
            emit(Result.Success(movies))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Unknown error occurred"))
        }
    }
}