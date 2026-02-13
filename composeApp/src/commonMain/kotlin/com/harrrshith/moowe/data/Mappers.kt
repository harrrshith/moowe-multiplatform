package com.harrrshith.moowe.data

import com.harrrshith.moowe.data.local.entity.MovieEntity
import com.harrrshith.moowe.data.remote.dto.MovieDto
import com.harrrshith.moowe.domain.model.Movie

fun MovieDto.toDomain(): Movie {
    return Movie(
        id = id,
        title = name ?: title ?: "",
        overview = overview ?: "",
        posterPath = posterPath ?: "",
        backdropPath = backdropPath ?: "",
        releaseDate = firstAirDate ?: releaseDate ?: "",
        voteAverage = voteAverage,
        voteCount = voteCount,
        popularity = popularity,
        adult = adult,
        genreIds = genreIds
    )
}

fun MovieDto.toEntity(mediaType: String = "movie", genreId: Int = 0) : MovieEntity {
    return MovieEntity(
        cacheKey = "$mediaType-$genreId-${this.id}",
        id = this.id,
        title = this.name ?: this.title ?: "",
        overview = this.overview ?: "",
        posterPath = this.posterPath,
        backdropPath = this.backdropPath,
        releaseDate = this.firstAirDate ?: this.releaseDate,
        voteAverage = this.voteAverage,
        voteCount = this.voteCount,
        popularity = this.popularity,
        adult = this.adult,
        genreIds = this.genreIds,
        mediaType = mediaType,
        cachedAt = 0L
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