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
    
    override suspend fun getTrendingMovies(): Result<List<Movie>> {
        return try {
            val movies = withContext(Dispatchers.Default) {
                val response = api.getTrendingMovies()
                val entities = response.movies.map { it.toEntity().copy(genre = Genre.TRENDING.id) }
                entities.map { it.copy(genre = Genre.TRENDING.id) }
                dao.insertMovies(entities)
                dao.getAllMovies().map { it.toDomain() }
            }
            Result.Success(movies)
        } catch (e: Exception) {
            return try {
                val cachedMovies = dao.getAllMovies().map { it.toDomain() }
                if (cachedMovies.isNotEmpty()) {
                    Result.Success(cachedMovies)
                } else {
                    Result.Error(e.message ?: "No cached data available", Int.MAX_VALUE)
                }
            } catch (cacheException: Exception) {
                Result.Error(e.message ?: "Unknown error occurred", Int.MAX_VALUE)
            }
        }
    }

    override suspend fun getMoviesByGenre(genre: Genre): Result<List<Movie>> {
        return try {
            val movies = withContext(Dispatchers.Default) {
                val response = api.getMoviesByGenre(genreId = genre.id)
                val entities = response.movies.map { it.toEntity().copy(genre = genre.id) }
                entities.map { it.copy(genre = Genre.TRENDING.id) }
                dao.insertMovies(entities)
                dao.getMoviesByGenre(id = genre.id).map { it.toDomain() }
            }
            Result.Success(movies)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown error occurred", Int.MAX_VALUE)
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