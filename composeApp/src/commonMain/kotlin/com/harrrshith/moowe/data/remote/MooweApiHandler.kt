package com.harrrshith.moowe.data.remote

import com.harrrshith.moowe.data.remote.dto.MoviesResponse
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
}