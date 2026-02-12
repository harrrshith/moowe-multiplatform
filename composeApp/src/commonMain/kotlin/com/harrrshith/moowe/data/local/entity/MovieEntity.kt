package com.harrrshith.moowe.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "movies", primaryKeys = ["cacheKey"])
data class MovieEntity(
    val cacheKey: String, // Composite key: "mediaType-genre-id"
    val id: Int,
    val title: String,
    val overview: String,
    @SerialName("poster_path") val posterPath: String? = null,
    @SerialName("backdrop_path") val backdropPath: String? = null,
    @SerialName("release_date") val releaseDate: String? = "",
    @SerialName("vote_average") val voteAverage: Double = 0.0,
    @SerialName("vote_count") val voteCount: Int = 0,
    val popularity: Double = 0.0,
    val adult: Boolean = false,
    @SerialName("genre_ids") val genreIds: List<Int> = emptyList(),
    @SerialName("genre") var genre: Int? = Int.MIN_VALUE,
    val mediaType: String = "movie", // "movie" or "tv"
    
    // Simple cache timestamp
    val cachedAt: Long
)