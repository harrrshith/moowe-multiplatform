package com.harrrshith.moowe.domain.model

data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String,
    val backdropPath: String,
    val releaseDate: String,
    val voteAverage: Double,
    val voteCount: Int,
    val popularity: Double,
    val adult: Boolean,
    val genreIds: List<Int>,
    val mediaType: MediaType = MediaType.MOVIE,
    val numberOfSeasons: Int? = null,
    val numberOfEpisodes: Int? = null,
    val seasons: List<Season> = emptyList(),
)
