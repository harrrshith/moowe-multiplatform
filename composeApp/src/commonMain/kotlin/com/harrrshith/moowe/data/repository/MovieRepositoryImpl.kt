package com.harrrshith.moowe.data.repository

import com.harrrshith.moowe.data.local.CacheConfig
import com.harrrshith.moowe.data.local.MooweDao
import com.harrrshith.moowe.data.remote.MooweApiHandler
import com.harrrshith.moowe.data.toDomain
import com.harrrshith.moowe.data.toEntity
import com.harrrshith.moowe.domain.model.Genre
import com.harrrshith.moowe.domain.model.Movie
import com.harrrshith.moowe.domain.repository.MovieRepository
import com.harrrshith.moowe.domain.utility.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class MovieRepositoryImpl(
    private val api: MooweApiHandler,
    private val dao: MooweDao
) : MovieRepository {

    override fun getTrendingMovies(): Flow<Result<List<Movie>>> = flow {
        // Check if cached data exists and is still valid
        val cachedMovies = dao.getMoviesByGenre(id = Genre.TRENDING.id).firstOrNull()
        val isCacheValid = cachedMovies?.firstOrNull()?.let { movie ->
            System.currentTimeMillis() - movie.cachedAt < CacheConfig.CACHE_EXPIRATION_TIME
        } ?: false
        
        // If cache is valid, return it
        if (isCacheValid && cachedMovies!!.isNotEmpty()) {
            emitAll(
                dao.getMoviesByGenre(id = Genre.TRENDING.id)
                    .map { Result.Success(processMovies(it)) }
            )
        } else {
            // Otherwise fetch from network
            try {
                val response = api.getTrendingMovies()
                val entities = response.movies.map { 
                    it.toEntity().copy(
                        genre = Genre.TRENDING.id,
                        cachedAt = System.currentTimeMillis()
                    )
                }
                dao.insertMovies(entities)
                
                // Emit fresh data from cache
                emitAll(
                    dao.getMoviesByGenre(id = Genre.TRENDING.id)
                        .map { Result.Success(processMovies(it)) }
                )
            } catch (e: Exception) {
                // On error, fallback to expired cache if available
                if (!cachedMovies.isNullOrEmpty()) {
                    emit(Result.Success(processMovies(cachedMovies)))
                } else {
                    emit(Result.Error(e.message ?: "No cached data available", Int.MAX_VALUE))
                }
            }
        }
    }.catch { e ->
        emit(Result.Error(e.message ?: "Unknown error", Int.MAX_VALUE))
    }.flowOn(Dispatchers.IO)

    override fun getMoviesByGenre(genre: Genre): Flow<Result<List<Movie>>> = flow {
        // Check if cached data exists and is still valid
        val cachedMovies = dao.getMoviesByGenre(id = genre.id).firstOrNull()
        val isCacheValid = cachedMovies?.firstOrNull()?.let { movie ->
            System.currentTimeMillis() - movie.cachedAt < CacheConfig.CACHE_EXPIRATION_TIME
        } ?: false
        
        // If cache is valid, return it
        if (isCacheValid && cachedMovies!!.isNotEmpty()) {
            emitAll(
                dao.getMoviesByGenre(id = genre.id)
                    .map { Result.Success(processMovies(it)) }
            )
        } else {
            // Otherwise fetch from network
            try {
                val response = api.getMoviesByGenre(genreId = genre.id)
                val entities = response.movies.map { 
                    it.toEntity().copy(
                        genre = genre.id,
                        cachedAt = System.currentTimeMillis()
                    )
                }
                dao.insertMovies(entities)
                
                // Emit fresh data from cache
                emitAll(
                    dao.getMoviesByGenre(id = genre.id)
                        .map { Result.Success(processMovies(it)) }
                )
            } catch (e: Exception) {
                // On error, fallback to expired cache if available
                if (!cachedMovies.isNullOrEmpty()) {
                    emit(Result.Success(processMovies(cachedMovies)))
                } else {
                    emit(Result.Error(e.message ?: "No cached data available", Int.MAX_VALUE))
                }
            }
        }
    }.catch { e ->
        emit(Result.Error(e.message ?: "Unknown error", Int.MAX_VALUE))
    }.flowOn(Dispatchers.IO)

    private fun processMovies(entities: List<com.harrrshith.moowe.data.local.entity.MovieEntity>): List<Movie> {
        return entities.map { it.toDomain() }
            .distinctBy { it.id }
            .sortedWith(
                compareByDescending<Movie> { it.popularity }
                    .thenByDescending { it.voteAverage }
            )
    }

    // Additional methods for local database operations
    fun getMoviesFlow(): Flow<List<Movie>> {
        return dao.getAllMoviesFlow().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getMovieById(id: Int): Result<Movie> {
        return try {
            val movie = dao.getMovieById(movieId = id)
            if (movie != null) {
                Result.Success(movie.toDomain())
            } else {
                Result.Error("Movie not found", status = 404)
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown error", status = 500)
        }
    }
    
    suspend fun clearCache() {
        dao.clearAllMovies()
    }
    
    suspend fun getCachedMovieCount(): Int {
        return dao.getMovieCount()
    }
}