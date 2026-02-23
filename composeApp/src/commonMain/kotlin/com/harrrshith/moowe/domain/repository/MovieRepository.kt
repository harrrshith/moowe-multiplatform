package com.harrrshith.moowe.domain.repository

import androidx.paging.PagingData
import com.harrrshith.moowe.domain.model.Genre
import com.harrrshith.moowe.domain.model.MediaType
import com.harrrshith.moowe.domain.model.CastMember
import com.harrrshith.moowe.domain.model.Movie
import com.harrrshith.moowe.domain.model.Review
import com.harrrshith.moowe.domain.utility.Result
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getTrendingMovies(): Flow<Result<List<Movie>>>

    fun getMoviesByGenre(genre: Genre): Flow<Result<List<Movie>>>

    fun getTrendingMedia(mediaType: MediaType, forceRefresh: Boolean = false): Flow<Result<List<Movie>>>

    fun getMediaByGenre(mediaType: MediaType, genre: Genre, forceRefresh: Boolean = false): Flow<Result<List<Movie>>>

    fun getTrendingPagedMedia(
        mediaType: MediaType,
        genre: Genre = Genre.TRENDING,
        pageSize: Int = 20,
    ): Flow<PagingData<Movie>>

    suspend fun getMovieById(id: Int): Result<Movie>

    suspend fun getMovieReviews(movieId: Int): Result<List<Review>>

    suspend fun getMovieCast(movieId: Int): Result<List<CastMember>>

    suspend fun getRelatedMovies(movieId: Int): Result<List<Movie>>
}
