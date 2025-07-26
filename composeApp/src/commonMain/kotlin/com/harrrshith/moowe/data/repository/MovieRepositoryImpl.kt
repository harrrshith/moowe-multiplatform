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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class MovieRepositoryImpl(
    private val api: MooweApiHandler,
    private val dao: MooweDao
) : MovieRepository {
    
    override suspend fun getTrendingMovies(): Result<List<Movie>> {
        return try {
            // Try to fetch from network first
            val networkResult = withContext(Dispatchers.Default) {
                val response = api.getTrendingMovies()
                val movies = response.movies.map { it.toDomain() }
                val entities = response.movies.map { it.toEntity() }
                
                // Cache movies in local database
                dao.insertMovies(entities)
                
                movies
            }
            Result.Success(networkResult)
        } catch (e: Exception) {
            // If network fails, try to load from cache
            return try {
                val cachedMovies = dao.getAllMovies().first().map { it.toDomain() }
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
                val movies = response.movies.map { it.toDomain() }
                val entities = response.movies.map { it.toEntity() }
                
                // Cache movies in local database
                dao.insertMovies(entities)
                
                movies
            }
            Result.Success(movies)
        } catch (e: Exception) {
            // Fallback to cached data for genre filtering would require additional logic
            // For now, return error
            Result.Error(e.message ?: "Unknown error occurred", Int.MAX_VALUE)
        }
    }
    
    // Additional methods for local database operations
    fun getMoviesFlow(): Flow<List<Movie>> {
        return dao.getAllMovies().map { entities ->
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