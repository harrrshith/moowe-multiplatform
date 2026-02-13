package com.harrrshith.moowe.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDto(
    val id: Int,
    val title: String? = null,  // For movies
    val name: String? = null,   // For TV series
    val overview: String? = "",
    @SerialName("poster_path") val posterPath: String? = null,
    @SerialName("backdrop_path") val backdropPath: String? = null,
    @SerialName("release_date") val releaseDate: String? = "",  // For movies
    @SerialName("first_air_date") val firstAirDate: String? = "",  // For TV series
    @SerialName("vote_average") val voteAverage: Double = 0.0,
    @SerialName("vote_count") val voteCount: Int = 0,
    val popularity: Double = 0.0,
    val adult: Boolean = false,
    @SerialName("genre_ids") val genreIds: List<Int> = emptyList()
)