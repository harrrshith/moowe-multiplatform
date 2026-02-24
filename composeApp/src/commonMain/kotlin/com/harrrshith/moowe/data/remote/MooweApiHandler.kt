package com.harrrshith.moowe.data.remote

import com.harrrshith.moowe.data.remote.dto.CreditsResponse
import com.harrrshith.moowe.data.remote.dto.MediaDetailDto
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
        region: String = "IN",
        page: Int = 1,
    ): MoviesResponse {
        return client.get("trending/$mediaType/$timeWindow") {
            parameter("language", language)
            parameter("include_adult", false)
            parameter("region", region)
            parameter("page", page)
        }.body()
    }

    suspend fun getMediaByGenre(
        mediaType: String = "movie",
        genreId: Int,
        language: String = "en-IN",
        region: String = "IN",
        page: Int = 1,
    ): MoviesResponse {
        return client.get("discover/$mediaType") {
            parameter("language", language)
            parameter("with_genres", genreId)
            parameter("include_adult", false)
            parameter("region", region)
            parameter("page", page)
        }.body()
    }

    // Backward compatibility methods
    suspend fun getTrendingMovies(
        mediaType: String = "movie",
        timeWindow: String = "day",
        language: String = "en-IN",
        region: String = "IN",
        page: Int = 1,
    ): MoviesResponse = getTrendingMedia(mediaType, timeWindow, language, region, page)

    suspend fun getMoviesByGenre(
        genreId: Int,
        language: String = "en-IN",
        region: String = "IN",
        page: Int = 1,
    ): MoviesResponse = getMediaByGenre("movie", genreId, language, region, page)

    suspend fun getMediaDetails(
        mediaType: String,
        mediaId: Int,
        language: String = "en-US",
    ): MediaDetailDto {
        return client.get("$mediaType/$mediaId") {
            parameter("language", language)
        }.body()
    }

    suspend fun getMediaReviews(
        mediaType: String,
        mediaId: Int,
        language: String = "en-US",
        page: Int = 1,
    ): ReviewsResponse {
        return client.get("$mediaType/$mediaId/reviews") {
            parameter("language", language)
            parameter("page", page)
        }.body()
    }

    suspend fun getMediaCredits(
        mediaType: String,
        mediaId: Int,
        language: String = "en-US",
    ): CreditsResponse {
        return client.get("$mediaType/$mediaId/credits") {
            parameter("language", language)
        }.body()
    }

    suspend fun getSimilarMedia(
        mediaType: String,
        mediaId: Int,
        language: String = "en-US",
        page: Int = 1,
    ): MoviesResponse {
        return client.get("$mediaType/$mediaId/similar") {
            parameter("language", language)
            parameter("page", page)
        }.body()
    }
}
