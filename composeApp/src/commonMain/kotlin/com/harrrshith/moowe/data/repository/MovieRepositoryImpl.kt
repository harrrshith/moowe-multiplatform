package com.harrrshith.moowe.data.repository

import com.harrrshith.moowe.data.local.MooweDao
import com.harrrshith.moowe.data.remote.MooweApiHandler
import com.harrrshith.moowe.data.toDomain
import com.harrrshith.moowe.data.toEntity
import com.harrrshith.moowe.domain.model.Genre
import com.harrrshith.moowe.domain.model.Movie
import com.harrrshith.moowe.domain.repository.MovieRepository
import com.harrrshith.moowe.domain.utility.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class MovieRepositoryImpl(
    private val api: MooweApiHandler,
    private val dao: MooweDao
) : MovieRepository {
    
    override fun getTrendingMovies(): Flow<Result<List<Movie>>> = flow {
        try {
            val movies = withContext(Dispatchers.Default) {
                val response = api.getTrendingMovies()
                val entities = response.movies.map { it.toEntity().copy(genre = Genre.TRENDING.id) }
                dao.insertMovies(entities)
                dao.getMoviesByGenre(id = Genre.TRENDING.id)
                    .map { it.toDomain() }
                    .distinctBy { it.id }
                    .sortedWith(compareByDescending<Movie> { it.popularity }
                        .thenByDescending { it.voteAverage })
            }
            emit(Result.Success(movies))
        } catch (e: Exception) {
            emit(handleError(e) { dao.getMoviesByGenre(Genre.TRENDING.id).map { it.toDomain() } })
        }
    }

    override fun getMoviesByGenre(genre: Genre): Flow<Result<List<Movie>>> = flow{
        try {
            val movies = withContext(Dispatchers.Default) {
                val response = api.getMoviesByGenre(genreId = genre.id)
                val entities = response.movies.map { it.toEntity().copy(genre = genre.id) }
                dao.insertMovies(entities)
                dao.getMoviesByGenre(id = genre.id)
                    .map { it.toDomain() }
                    .distinctBy { it.id }
                    .sortedWith(compareByDescending<Movie> { it.popularity }
                        .thenByDescending { it.voteAverage })
            }
            emit(Result.Success(movies))
        } catch (e: Exception) {
            emit(handleError(e) { dao.getMoviesByGenre(genre.id).map { it.toDomain() } })
        }
    }

    private suspend fun handleError(
        exception: Exception,
        fallback: suspend () -> List<Movie>
    ): Result<List<Movie>> {
        return try {
            val cachedMovies = fallback()
            if (cachedMovies.isNotEmpty()) {
                Result.Success(cachedMovies)
            } else {
                Result.Error(exception.message ?: "No cached data available", Int.MAX_VALUE)
            }
        } catch (exception: Exception) {
            Result.Error(exception.message ?: "Unknown error occurred", Int.MAX_VALUE)
        }
    }

    // Additional methods for local database operations
    fun getMoviesFlow(): Flow<List<Movie>> {
        return dao.getAllMoviesFlow().map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    suspend fun getMovieById(movieId: Int): Movie? {
        return dao.getMovieById(movieId)?.toDomain()
    }
    
    suspend fun clearCache() {
        dao.clearAllMovies()
    }
    
    suspend fun getCachedMovieCount(): Int {
        return dao.getMovieCount()
    }
}