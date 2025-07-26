package com.harrrshith.moowe.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.harrrshith.moowe.data.local.entity.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MooweDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<MovieEntity>)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: MovieEntity)

    @Query("SELECT * FROM movies")
    fun getAllMovies(): Flow<List<MovieEntity>>
    
    @Query("SELECT * FROM movies WHERE id = :movieId")
    suspend fun getMovieById(movieId: Int): MovieEntity?
    
    @Query("DELETE FROM movies")
    suspend fun clearAllMovies()
    
    @Query("DELETE FROM movies WHERE id = :movieId")
    suspend fun deleteMovieById(movieId: Int)
    
    @Query("SELECT COUNT(*) FROM movies")
    suspend fun getMovieCount(): Int
}