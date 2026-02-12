package com.harrrshith.moowe.domain.repository

import com.harrrshith.moowe.domain.model.Genre
import com.harrrshith.moowe.domain.model.MediaType
import com.harrrshith.moowe.domain.model.Movie
import com.harrrshith.moowe.domain.utility.Result
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getTrendingMovies(): Flow<Result<List<Movie>>>

    fun getMoviesByGenre(genre: Genre): Flow<Result<List<Movie>>>

    fun getTrendingMedia(mediaType: MediaType): Flow<Result<List<Movie>>>

    fun getMediaByGenre(mediaType: MediaType, genre: Genre): Flow<Result<List<Movie>>>

    suspend fun getMovieById(id: Int): Result<Movie>
}