package com.harrrshith.moowe.data.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class MovieApi(
    private val client: HttpClient = NetworkService.httpClient
) {
    suspend fun getTrendingMovies(
        mediaType: String = "movie",
        timeWindow: String = "day",
        language: String = "en-US"
    ): TrendingMoviesResponse {
        return client.get("trending/$mediaType/$timeWindow") {
            parameter("language", language)
        }.body()
    }
}


//For now we'll keep this here
@Serializable
data class MovieDto(
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
    @SerialName("genre_ids") val genreIds: List<Int> = emptyList()
)

@Serializable
data class TrendingMoviesResponse(
    val page: Int,
    @SerialName("results") val movies: List<MovieDto>,
    @SerialName("total_pages") val totalPages: Int,
    @SerialName("total_results") val totalResults: Int
)

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
    val genreIds: List<Int>
)

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
}

interface MovieRepository {
    fun getTrendingMovies(): Flow<Result<List<Movie>>>
}

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val message: String, val code: Int = 0) : Result<Nothing>()
    data object Loading : Result<Nothing>()
}

class MovieRepositoryImpl(
    private val api: MovieApi
) : MovieRepository {

    override fun getTrendingMovies(): Flow<Result<List<Movie>>> = flow {
        emit(Result.Loading)
        try {
            val response = api.getTrendingMovies()
            val movies = response.movies.map { it.toDomain() }
            emit(Result.Success(movies))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Unknown error occurred"))
        }
    }
}