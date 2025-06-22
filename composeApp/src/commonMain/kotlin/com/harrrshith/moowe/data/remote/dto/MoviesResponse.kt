package com.harrrshith.moowe.data.remote.dto

import kotlinx.serialization.SerialName

data class MoviesResponse(
    val page: Int,
    @SerialName("results") val movies: List<MovieDto>,
    @SerialName("total_pages") val totalPages: Int,
    @SerialName("total_results") val totalResults: Int
)