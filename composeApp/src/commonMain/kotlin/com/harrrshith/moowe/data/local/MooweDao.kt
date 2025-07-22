package com.harrrshith.moowe.data.local

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.harrrshith.moowe.data.local.entity.MovieEntity
import com.harrrshith.moowe.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MooweDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: List<MovieEntity>)

    fun fetchAllMovies() : Flow<List<Movie>>
}