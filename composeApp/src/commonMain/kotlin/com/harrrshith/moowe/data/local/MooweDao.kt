package com.harrrshith.moowe.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.harrrshith.moowe.data.local.entity.MovieEntity
import com.harrrshith.moowe.data.local.entity.RecentSearchEntity
import kotlinx.coroutines.flow.Flow

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

    @Query("SELECT * FROM movies WHERE genre = :id AND mediaType = :mediaType")
    fun getMoviesByGenreAndType(id: Int, mediaType: String): Flow<List<MovieEntity>>
    
    @Query("SELECT * FROM movies WHERE genre = :id")
    fun getMoviesByGenre(id: Int): Flow<List<MovieEntity>>
    
    @Query("SELECT * FROM movies WHERE id = :movieId AND mediaType = :mediaType")
    suspend fun getMovieById(movieId: Int, mediaType: String): MovieEntity?
    
    @Query("DELETE FROM movies")
    suspend fun clearAllMovies()
    
    @Query("DELETE FROM movies WHERE id = :movieId")
    suspend fun deleteMovieById(movieId: Int)
    
    @Query("SELECT COUNT(*) FROM movies")
    suspend fun getMovieCount(): Int
    
    // Simple cache cleanup
    @Query("DELETE FROM movies WHERE cachedAt < :expirationThreshold")
    suspend fun deleteExpiredMovies(expirationThreshold: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecentSearch(search: RecentSearchEntity)

    @Query("SELECT * FROM recent_searches ORDER BY searchedAt DESC LIMIT :limit")
    fun getRecentSearches(limit: Int): Flow<List<RecentSearchEntity>>
}
