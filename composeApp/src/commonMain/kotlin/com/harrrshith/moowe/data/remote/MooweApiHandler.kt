package com.harrrshith.moowe.data.remote

import com.harrrshith.moowe.data.remote.dto.CreditsResponse
import com.harrrshith.moowe.data.remote.dto.MoviesResponse
import com.harrrshith.moowe.data.remote.dto.ReviewsResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class MooweApiHandler(
    private val client: HttpClient
) {
    suspend fun getTrendingMedia(
        mediaType: String = "movie",
        timeWindow: String = "day",
        language: String = "en-IN",
        region: String = "IN"
    ): MoviesResponse {
        return client.get("trending/$mediaType/$timeWindow") {
            parameter("language", language)
            parameter("include_adult", false)
            parameter("region", region)
        }.body()
    }

    suspend fun getMediaByGenre(
        mediaType: String = "movie",
        genreId: Int,
        language: String = "en-IN",
        region: String = "IN"
    ): MoviesResponse {
        return client.get("discover/$mediaType") {
            parameter("language", language)
            parameter("with_genres", genreId)
            parameter("include_adult", false)
            parameter("region", region)
        }.body()
    }

    // Backward compatibility methods
    suspend fun getTrendingMovies(
        mediaType: String = "movie",
        timeWindow: String = "day",
        language: String = "en-IN",
        region: String = "IN"
    ): MoviesResponse = getTrendingMedia(mediaType, timeWindow, language, region)

    suspend fun getMoviesByGenre(
        genreId: Int,
        language: String = "en-IN",
        region: String = "IN"
    ): MoviesResponse = getMediaByGenre("movie", genreId, language, region)

    suspend fun getMovieReviews(
        movieId: Int,
        language: String = "en-US",
        page: Int = 1,
    ): ReviewsResponse {
        return client.get("movie/$movieId/reviews") {
            parameter("language", language)
            parameter("page", page)
        }.body()
    }

    suspend fun getMovieCredits(
        movieId: Int,
        language: String = "en-US",
    ): CreditsResponse {
        return client.get("movie/$movieId/credits") {
            parameter("language", language)
        }.body()
    }

    suspend fun getSimilarMovies(
        movieId: Int,
        language: String = "en-US",
        page: Int = 1,
    ): MoviesResponse {
        return client.get("movie/$movieId/similar") {
            parameter("language", language)
            parameter("page", page)
        }.body()
    }
}
