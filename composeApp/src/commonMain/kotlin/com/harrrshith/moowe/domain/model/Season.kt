package com.harrrshith.moowe.domain.model

data class Season(
    val id: Int,
    val name: String,
    val seasonNumber: Int,
    val episodeCount: Int,
    val airDate: String,
    val posterPath: String,
    val overview: String,
)
