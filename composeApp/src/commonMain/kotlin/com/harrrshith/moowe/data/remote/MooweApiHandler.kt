package com.harrrshith.moowe.data.remote

import com.harrrshith.moowe.data.remote.dto.MoviesResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class MooweApiHandler(
    private val client: HttpClient
) {
    suspend fun getTrendingMovies(
        mediaType: String = "movie",
        timeWindow: String = "day",
        language: String = "en-IN"
    ): MoviesResponse {
        return client.get("trending/$mediaType/$timeWindow") {
            parameter("language", language)
        }.body()
    }

    suspend fun getMoviesByGenre(
        genreId: Int,
        language: String = "en-IN"
    ): MoviesResponse {
        return client.get("discover/movie") {
            parameter("language", language)
            parameter("with_genres", genreId)
            parameter("include_adult", false)
        }.body()
    }
}