package com.harrrshith.moowe.data.local.entity

import androidx.room.Entity

@Entity(tableName = "favorites", primaryKeys = ["cacheKey"])
data class FavoriteEntity(
    val cacheKey: String,
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
    val mediaType: String,
    val createdAt: Long,
)
