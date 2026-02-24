package com.harrrshith.moowe.data

import com.harrrshith.moowe.data.local.entity.MovieEntity
import com.harrrshith.moowe.data.local.entity.FavoriteEntity
import com.harrrshith.moowe.data.local.entity.RecentSearchEntity
import com.harrrshith.moowe.data.remote.dto.CastDto
import com.harrrshith.moowe.data.remote.dto.MediaDetailDto
import com.harrrshith.moowe.data.remote.dto.MovieDto
import com.harrrshith.moowe.data.remote.dto.ReviewDto
import com.harrrshith.moowe.domain.model.CastMember
import com.harrrshith.moowe.domain.model.MediaType
import com.harrrshith.moowe.domain.model.Movie
import com.harrrshith.moowe.domain.model.Review
import com.harrrshith.moowe.domain.model.Season
import kotlin.time.Clock

fun MovieDto.toDomain(mediaType: MediaType = MediaType.MOVIE): Movie {
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
        genreIds = genreIds,
        mediaType = mediaType,
    )
}

fun MediaDetailDto.toDomain(mediaType: MediaType): Movie {
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
        genreIds = genres.map { it.id },
        mediaType = mediaType,
        numberOfSeasons = numberOfSeasons,
        numberOfEpisodes = numberOfEpisodes,
        seasons = seasons.map { season ->
            Season(
                id = season.id,
                name = season.name ?: "",
                seasonNumber = season.seasonNumber,
                episodeCount = season.episodeCount,
                airDate = season.airDate ?: "",
                posterPath = season.posterPath ?: "",
                overview = season.overview ?: "",
            )
        },
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

fun ReviewDto.toDomain(): Review = Review(
    id = id,
    author = author,
    authorUsername = authorDetails.username,
    avatarPath = authorDetails.avatarPath,
    rating = authorDetails.rating,
    content = content,
    createdAt = createdAt,
)

fun CastDto.toDomain(): CastMember = CastMember(
    id = id,
    name = name,
    character = character,
    profilePath = profilePath,
    order = order,
)

fun MovieEntity.toDomain() : Movie {
    val mediaTypeValue = if (mediaType == MediaType.TV_SERIES.apiValue) {
        MediaType.TV_SERIES
    } else {
        MediaType.MOVIE
    }

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
        genreIds = genreIds,
        mediaType = mediaTypeValue,
    )
}

fun Movie.toRecentSearchEntity(): RecentSearchEntity {
    return RecentSearchEntity(
        cacheKey = "${mediaType.apiValue}-$id",
        id = id,
        title = title,
        posterPath = posterPath,
        releaseDate = releaseDate,
        mediaType = mediaType.apiValue,
        searchedAt = Clock.System.now().toEpochMilliseconds(),
    )
}

fun RecentSearchEntity.toDomain(): Movie {
    return Movie(
        id = id,
        title = title,
        overview = "",
        posterPath = posterPath,
        backdropPath = "",
        releaseDate = releaseDate,
        voteAverage = 0.0,
        voteCount = 0,
        popularity = 0.0,
        adult = false,
        genreIds = emptyList(),
        mediaType = MediaType.fromApiValue(mediaType),
    )
}

fun Movie.toFavoriteEntity(): FavoriteEntity {
    return FavoriteEntity(
        cacheKey = "${mediaType.apiValue}-$id",
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath,
        backdropPath = backdropPath,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        voteCount = voteCount,
        popularity = popularity,
        adult = adult,
        genreIds = genreIds,
        mediaType = mediaType.apiValue,
        createdAt = Clock.System.now().toEpochMilliseconds(),
    )
}

fun FavoriteEntity.toDomain(): Movie {
    return Movie(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath,
        backdropPath = backdropPath,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        voteCount = voteCount,
        popularity = popularity,
        adult = adult,
        genreIds = genreIds,
        mediaType = MediaType.fromApiValue(mediaType),
    )
}
