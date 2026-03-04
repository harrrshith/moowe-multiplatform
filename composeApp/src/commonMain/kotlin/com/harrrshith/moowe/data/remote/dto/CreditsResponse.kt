package com.harrrshith.moowe.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CastDto(
    val id: Int,
    val name: String = "",
    val character: String = "",
    @SerialName("profile_path") val profilePath: String? = null,
    val order: Int = Int.MAX_VALUE,
)

@Serializable
data class CreditsResponse(
    val id: Int,
    val cast: List<CastDto> = emptyList(),
)

@Serializable
data class CombinedCreditDto(
    val id: Int,
    val title: String? = null,
    val name: String? = null,
    val overview: String? = "",
    @SerialName("poster_path") val posterPath: String? = null,
    @SerialName("backdrop_path") val backdropPath: String? = null,
    @SerialName("release_date") val releaseDate: String? = "",
    @SerialName("first_air_date") val firstAirDate: String? = "",
    @SerialName("vote_average") val voteAverage: Double = 0.0,
    @SerialName("vote_count") val voteCount: Int = 0,
    val popularity: Double = 0.0,
    val adult: Boolean = false,
    @SerialName("genre_ids") val genreIds: List<Int> = emptyList(),
    @SerialName("media_type") val mediaType: String = "",
)

@Serializable
data class CombinedCreditsResponse(
    val id: Int,
    val cast: List<CombinedCreditDto> = emptyList(),
)
