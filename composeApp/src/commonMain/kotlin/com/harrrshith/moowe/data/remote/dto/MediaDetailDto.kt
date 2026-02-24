package com.harrrshith.moowe.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenreItemDto(
    val id: Int = 0,
)

@Serializable
data class SeasonDto(
    val id: Int,
    val name: String? = "",
    @SerialName("season_number") val seasonNumber: Int = 0,
    @SerialName("episode_count") val episodeCount: Int = 0,
    @SerialName("air_date") val airDate: String? = "",
    @SerialName("poster_path") val posterPath: String? = null,
    val overview: String? = "",
)

@Serializable
data class MediaDetailDto(
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
    val genres: List<GenreItemDto> = emptyList(),
    @SerialName("number_of_seasons") val numberOfSeasons: Int? = null,
    @SerialName("number_of_episodes") val numberOfEpisodes: Int? = null,
    val seasons: List<SeasonDto> = emptyList(),
)
