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
    private val dao: MooweDao,
    private val cacheConfig: CacheConfig = CacheConfig.DEFAULT
) : MovieRepository {

    override fun getTrendingMovies(): Flow<Result<List<Movie>>> = flow {
        // Step 1: Check cache first
        val cachedMovies = dao.getMoviesByGenre(id = Genre.TRENDING.id).firstOrNull()
        val latestCacheTime = dao.getLatestCacheTimeForGenre(Genre.TRENDING.id)
        
        // Step 2: Determine cache freshness
        val isCacheExpired = latestCacheTime?.let { 
            System.currentTimeMillis() - it > cacheConfig.expirationTimeMillis 
        } ?: true
        
        val isCacheStale = latestCacheTime?.let { 
            System.currentTimeMillis() - it > cacheConfig.staleTimeMillis 
        } ?: true
        
        // Step 3: Emit cached data immediately if available and valid
        if (cachedMovies != null && cachedMovies.isNotEmpty() && !isCacheExpired) {
            if (cacheConfig.staleWhileRevalidate) {
                // Emit cached data immediately
                emit(Result.Success(processCachedMovies(cachedMovies)))
                
                // If stale, fetch fresh data in background
                if (isCacheStale) {
                    fetchAndCacheTrendingMovies()
                }
            } else {
                // If not stale, just return cached data
                if (!isCacheStale) {
                    emitAll(
                        dao.getMoviesByGenre(id = Genre.TRENDING.id)
                            .map { Result.Success(processCachedMovies(it)) }
                    )
                    return@flow
                }
            }
        }
        
        // Step 4: Fetch from network if cache is expired or empty
        if (isCacheExpired || cachedMovies.isNullOrEmpty()) {
            try {
                fetchAndCacheTrendingMovies()
                // Emit fresh data from cache
                emitAll(
                    dao.getMoviesByGenre(id = Genre.TRENDING.id)
                        .map { Result.Success(processCachedMovies(it)) }
                )
            } catch (e: Exception) {
                // Fallback to expired cache if network fails
                if (cachedMovies != null && cachedMovies.isNotEmpty()) {
                    emit(Result.Success(processCachedMovies(cachedMovies)))
                } else {
                    emit(Result.Error(e.message ?: "No cached data available", Int.MAX_VALUE))
                }
            }
        } else {
            // Emit live updates from cache
            emitAll(
                dao.getMoviesByGenre(id = Genre.TRENDING.id)
                    .map { Result.Success(processCachedMovies(it)) }
            )
        }
    }.catch { e ->
        emit(handleError(e as? Exception ?: Exception(e.message)))
    }.flowOn(Dispatchers.IO)
    
    private suspend fun fetchAndCacheTrendingMovies() {
        // Clean up old cache if needed
        manageCacheSize()
        
        val response = api.getTrendingMovies()
        val currentTime = System.currentTimeMillis()
        val entities = response.movies.map { 
            it.toEntity().copy(
                genre = Genre.TRENDING.id,
                cachedAt = currentTime,
                lastAccessedAt = currentTime
            )
        }
        dao.insertMovies(entities)
    }
    
    private fun processCachedMovies(entities: List<com.harrrshith.moowe.data.local.entity.MovieEntity>): List<Movie> {
        return entities.map { it.toDomain() }
            .distinctBy { it.id }
            .sortedWith(
                compareByDescending<Movie> { it.popularity }
                    .thenByDescending { it.voteAverage }
            )
    }

    override fun getMoviesByGenre(genre: Genre): Flow<Result<List<Movie>>> = flow {
        // Step 1: Check cache first
        val cachedMovies = dao.getMoviesByGenre(id = genre.id).firstOrNull()
        val latestCacheTime = dao.getLatestCacheTimeForGenre(genre.id)
        
        // Step 2: Determine cache freshness
        val isCacheExpired = latestCacheTime?.let { 
            System.currentTimeMillis() - it > cacheConfig.expirationTimeMillis 
        } ?: true
        
        val isCacheStale = latestCacheTime?.let { 
            System.currentTimeMillis() - it > cacheConfig.staleTimeMillis 
        } ?: true
        
        // Step 3: Emit cached data immediately if available and valid
        if (cachedMovies != null && cachedMovies.isNotEmpty() && !isCacheExpired) {
            if (cacheConfig.staleWhileRevalidate) {
                // Emit cached data immediately
                emit(Result.Success(processCachedMovies(cachedMovies)))
                
                // If stale, fetch fresh data in background
                if (isCacheStale) {
                    fetchAndCacheMoviesByGenre(genre)
                }
            } else {
                // If not stale, just return cached data
                if (!isCacheStale) {
                    emitAll(
                        dao.getMoviesByGenre(id = genre.id)
                            .map { Result.Success(processCachedMovies(it)) }
                    )
                    return@flow
                }
            }
        }
        
        // Step 4: Fetch from network if cache is expired or empty
        if (isCacheExpired || cachedMovies.isNullOrEmpty()) {
            try {
                fetchAndCacheMoviesByGenre(genre)
                // Emit fresh data from cache
                emitAll(
                    dao.getMoviesByGenre(id = genre.id)
                        .map { Result.Success(processCachedMovies(it)) }
                )
            } catch (e: Exception) {
                // Fallback to expired cache if network fails
                if (cachedMovies != null && cachedMovies.isNotEmpty()) {
                    emit(Result.Success(processCachedMovies(cachedMovies)))
                } else {
                    emit(Result.Error(e.message ?: "No cached data available", Int.MAX_VALUE))
                }
            }
        } else {
            // Emit live updates from cache
            emitAll(
                dao.getMoviesByGenre(id = genre.id)
                    .map { Result.Success(processCachedMovies(it)) }
            )
        }
    }.catch { e ->
        emit(handleError(e as? Exception ?: Exception(e.message)))
    }.flowOn(Dispatchers.IO)
    
    private suspend fun fetchAndCacheMoviesByGenre(genre: Genre) {
        // Clean up old cache if needed
        manageCacheSize()
        
        val response = api.getMoviesByGenre(genreId = genre.id)
        val currentTime = System.currentTimeMillis()
        val entities = response.movies.map { 
            it.toEntity().copy(
                genre = genre.id,
                cachedAt = currentTime,
                lastAccessedAt = currentTime
            )
        }
        dao.insertMovies(entities)
    }

    private fun handleError(exception: Exception): Result<List<Movie>> {
        return Result.Error(
            exception.message ?: "Unknown error occurred", 
            Int.MAX_VALUE
        )
    }
    
    /**
     * Manages cache size using LRU eviction strategy
     */
    private suspend fun manageCacheSize() {
        try {
            val currentCount = dao.getMovieCount()
            if (currentCount >= cacheConfig.maxCacheSize) {
                // Remove 20% of oldest accessed items
                val itemsToRemove = (cacheConfig.maxCacheSize * 0.2).toInt()
                dao.deleteLeastRecentlyUsed(itemsToRemove)
            }
            
            // Also clean up expired entries
            val expirationThreshold = System.currentTimeMillis() - cacheConfig.expirationTimeMillis
            dao.deleteExpiredMovies(expirationThreshold)
        } catch (e: Exception) {
            // Silently fail cache cleanup to not disrupt main flow
        }
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
                // Update last accessed time for LRU tracking
                dao.updateLastAccessedTime(id)
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