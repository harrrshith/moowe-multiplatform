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

    suspend fun getMediaById(id: Int, mediaType: MediaType): Result<Movie>

    suspend fun getMediaReviews(mediaId: Int, mediaType: MediaType): Result<List<Review>>

    suspend fun getMediaCast(mediaId: Int, mediaType: MediaType): Result<List<CastMember>>

    suspend fun getRelatedMedia(mediaId: Int, mediaType: MediaType): Result<List<Movie>>
}
