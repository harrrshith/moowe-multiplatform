package com.harrrshith.moowe.data

import com.harrrshith.moowe.data.local.entity.MovieEntity
import com.harrrshith.moowe.data.remote.dto.MovieDto
import com.harrrshith.moowe.domain.model.Movie

fun MovieDto.toDomain(): Movie {
    return Movie(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath ?: "",
        backdropPath = backdropPath ?: "",
        releaseDate = releaseDate ?: "",
        voteAverage = voteAverage,
        voteCount = voteCount,
        popularity = popularity,
        adult = adult,
        genreIds = genreIds
    )
} // This goes later

fun MovieDto.toEntity() : MovieEntity {
    return MovieEntity(
        id = this.id,
        title = this.title,
        overview = this.overview,
        posterPath = this.posterPath,
        backdropPath = this.backdropPath,
        releaseDate = this.releaseDate,
        voteAverage = this.voteAverage,
        voteCount = this.voteCount,
        popularity = this.popularity,
        adult = this.adult,
        genreIds = this.genreIds,
        cachedAt = 0L // Will be set when inserting into DB
    )
}

fun MovieEntity.toDomain() : Movie {
    return Movie(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath ?: "",
        backdropPath = backdropPath ?: "",
        releaseDate = releaseDate ?: "",
        voteAverage = voteAverage,
        voteCount = voteCount,
        popularity = popularity,
        adult = adult,
        genreIds = genreIds
    )
}