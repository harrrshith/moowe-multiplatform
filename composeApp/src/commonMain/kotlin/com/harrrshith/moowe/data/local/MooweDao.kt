package com.harrrshith.moowe.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.harrrshith.moowe.data.local.entity.MovieEntity
import com.harrrshith.moowe.domain.model.Genre
import kotlinx.coroutines.flow.Flow
import kotlin.time.Clock

@Dao
interface MooweDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<MovieEntity>)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: MovieEntity)

    @Query("SELECT * FROM movies")
    fun getAllMoviesFlow(): Flow<List<MovieEntity>>

    @Query("SELECT * FROM movies")
    suspend fun getAllMovies(): List<MovieEntity>

    @Query("SELECT * FROM movies WHERE genre = :id")
    fun getMoviesByGenre(id: Int): Flow<List<MovieEntity>>
    
    @Query("SELECT * FROM movies WHERE id = :movieId")
    suspend fun getMovieById(movieId: Int): MovieEntity?
    
    @Query("DELETE FROM movies")
    suspend fun clearAllMovies()
    
    @Query("DELETE FROM movies WHERE id = :movieId")
    suspend fun deleteMovieById(movieId: Int)
    
    @Query("SELECT COUNT(*) FROM movies")
    suspend fun getMovieCount(): Int
    
    // Cache management methods
    
    @Query("UPDATE movies SET lastAccessedAt = :timestamp WHERE id = :movieId")
    suspend fun updateLastAccessedTime(movieId: Int, timestamp: Long = System.currentTimeMillis())
    
    @Query("DELETE FROM movies WHERE cachedAt < :expirationThreshold")
    suspend fun deleteExpiredMovies(expirationThreshold: Long)
    
    @Query("DELETE FROM movies WHERE id IN (SELECT id FROM movies ORDER BY lastAccessedAt ASC LIMIT :count)")
    suspend fun deleteLeastRecentlyUsed(count: Int)
    
    @Query("SELECT * FROM movies WHERE id = :movieId")
    suspend fun getMovieByIdWithAccess(movieId: Int): MovieEntity?
    
    @Query("SELECT cachedAt FROM movies WHERE genre = :genreId ORDER BY cachedAt DESC LIMIT 1")
    suspend fun getLatestCacheTimeForGenre(genreId: Int): Long?
    
    @Query("SELECT * FROM movies WHERE genre = :genreId AND cachedAt >= :minCacheTime")
    fun getNonExpiredMoviesByGenre(genreId: Int, minCacheTime: Long): Flow<List<MovieEntity>>
}