package com.harrrshith.moowe.data.repository

import androidx.paging.PagingData
import com.harrrshith.moowe.data.local.MooweDao
import com.harrrshith.moowe.data.paging.PageResult
import com.harrrshith.moowe.data.paging.createPagerFlow
import com.harrrshith.moowe.data.remote.MooweApiHandler
import com.harrrshith.moowe.data.toDomain
import com.harrrshith.moowe.data.toEntity
import com.harrrshith.moowe.domain.model.CastMember
import com.harrrshith.moowe.domain.model.Genre
import com.harrrshith.moowe.domain.model.MediaType
import com.harrrshith.moowe.domain.model.Movie
import com.harrrshith.moowe.domain.model.Review
import com.harrrshith.moowe.domain.repository.MovieRepository
import com.harrrshith.moowe.domain.utility.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class MovieRepositoryImpl(
    private val api: MooweApiHandler,
    private val dao: MooweDao
) : MovieRepository {

    override fun getTrendingMovies(): Flow<Result<List<Movie>>> = 
        dao.getMoviesByGenreAndType(id = Genre.TRENDING.id, mediaType = "movie")
            .map { movies -> Result.Success(processMovies(movies)) as Result<List<Movie>> }
            .onStart {
                // Fetch from network in background
                try {
                    val response = api.getTrendingMovies()
                    val entities = response.movies.map { 
                        it.toEntity(mediaType = "movie", genreId = Genre.TRENDING.id).copy(
                            genre = Genre.TRENDING.id,
                            cachedAt = 0L
                        )
                    }
                    dao.insertMovies(entities) // This triggers the Flow to emit updated data
                } catch (e: Exception) {
                    // Silently fail - we're already showing cached data
                    println("Error fetching trending movies: ${e.message}")
                }
            }
            .catch { e ->
                emit(Result.Error(e.message ?: "Unknown error", Int.MAX_VALUE))
            }
            .flowOn(Dispatchers.IO)

    override fun getMoviesByGenre(genre: Genre): Flow<Result<List<Movie>>> = 
        dao.getMoviesByGenreAndType(id = genre.id, mediaType = "movie")
            .map { movies -> Result.Success(processMovies(movies)) as Result<List<Movie>> }
            .onStart {
                // Fetch from network in background
                try {
                    val response = api.getMoviesByGenre(genreId = genre.id)
                    val entities = response.movies.map { 
                        it.toEntity(mediaType = "movie", genreId = genre.id).copy(
                            genre = genre.id,
                            cachedAt = 0L
                        )
                    }
                    dao.insertMovies(entities) // This triggers the Flow to emit updated data
                } catch (e: Exception) {
                    // Silently fail - we're already showing cached data
                    println("Error fetching movies by genre: ${e.message}")
                }
            }
            .catch { e ->
                emit(Result.Error(e.message ?: "Unknown error", Int.MAX_VALUE))
            }
            .flowOn(Dispatchers.IO)

    override fun getTrendingMedia(mediaType: MediaType, forceRefresh: Boolean): Flow<Result<List<Movie>>> =
        dao.getMoviesByGenreAndType(id = Genre.TRENDING.id, mediaType = mediaType.apiValue)
            .map { movies -> Result.Success(processMovies(movies)) as Result<List<Movie>> }
            .onStart {
                val hasCachedData = dao.getMoviesByGenreAndType(
                    id = Genre.TRENDING.id, mediaType = mediaType.apiValue
                ).firstOrNull()?.isNotEmpty() == true
                if (!hasCachedData || forceRefresh) {
                    try {
                        val response = api.getTrendingMedia(mediaType = mediaType.apiValue)
                        val entities = response.movies.map {
                            it.toEntity(mediaType = mediaType.apiValue, genreId = Genre.TRENDING.id).copy(
                                genre = Genre.TRENDING.id,
                                cachedAt = 0L
                            )
                        }
                        dao.insertMovies(entities)
                    } catch (e: Exception) {
                        println("Error fetching trending media: ${e.message}")
                    }
                }
            }
            .catch { e ->
                emit(Result.Error(e.message ?: "Unknown error", Int.MAX_VALUE))
            }
            .flowOn(Dispatchers.IO)

    override fun getMediaByGenre(mediaType: MediaType, genre: Genre, forceRefresh: Boolean): Flow<Result<List<Movie>>> =
        dao.getMoviesByGenreAndType(id = genre.id, mediaType = mediaType.apiValue)
            .map { movies -> Result.Success(processMovies(movies)) as Result<List<Movie>> }
            .onStart {
                val hasCachedData = dao.getMoviesByGenreAndType(
                    id = genre.id, mediaType = mediaType.apiValue
                ).firstOrNull()?.isNotEmpty() == true
                if (!hasCachedData || forceRefresh) {
                    try {
                        val genreIdForApi = genre.getIdForMediaType(mediaType)
                        val response = api.getMediaByGenre(mediaType = mediaType.apiValue, genreId = genreIdForApi)
                        val entities = response.movies.map {
                            it.toEntity(mediaType = mediaType.apiValue, genreId = genre.id).copy(
                                genre = genre.id,
                                cachedAt = 0L
                            )
                        }
                        dao.insertMovies(entities)
                    } catch (e: Exception) {
                        println("Error fetching media by genre: ${e.message}")
                    }
                }
            }
            .catch { e ->
                emit(Result.Error(e.message ?: "Unknown error", Int.MAX_VALUE))
            }
            .flowOn(Dispatchers.IO)

    override fun getTrendingPagedMedia(
        mediaType: MediaType,
        genre: Genre,
        pageSize: Int,
    ): Flow<PagingData<Movie>> {
        return createPagerFlow(pageSize = pageSize) { page, _ ->
            val response = if (genre == Genre.TRENDING) {
                api.getTrendingMedia(mediaType = mediaType.apiValue, page = page)
            } else {
                api.getMediaByGenre(
                    mediaType = mediaType.apiValue,
                    genreId = genre.getIdForMediaType(mediaType),
                    page = page,
                )
            }

            PageResult(
                items = response.movies.map { it.toDomain(mediaType = mediaType) },
                currentPage = response.page,
                totalPages = response.totalPages,
            )
        }
    }

    private fun processMovies(entities: List<com.harrrshith.moowe.data.local.entity.MovieEntity>): List<Movie> {
        return entities.map { it.toDomain() }
            .distinctBy { it.id }
            .sortedWith(
                compareByDescending<Movie> { it.popularity }
                    .thenByDescending { it.voteAverage }
            )
    }

    // Additional methods for local database operations
//    fun getMoviesFlow(): Flow<List<Movie>> {
//        return dao.getAllMoviesFlow().map { entities ->
//            entities.map { it.toDomain() }
//        }
//    }

    override suspend fun getMediaById(id: Int, mediaType: MediaType): Result<Movie> {
        return try {
            val cachedMedia = dao.getMovieById(movieId = id, mediaType = mediaType.apiValue)
            if (cachedMedia != null) {
                Result.Success(cachedMedia.toDomain())
            } else {
                val response = api.getMediaDetails(mediaType = mediaType.apiValue, mediaId = id)
                Result.Success(response.toDomain(mediaType = mediaType))
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown error", status = 500)
        }
    }

    override suspend fun getMediaReviews(mediaId: Int, mediaType: MediaType): Result<List<Review>> {
        return try {
            val response = api.getMediaReviews(mediaType = mediaType.apiValue, mediaId = mediaId)
            Result.Success(response.results.map { it.toDomain() })
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown error", status = 500)
        }
    }

    override suspend fun getMediaCast(mediaId: Int, mediaType: MediaType): Result<List<CastMember>> {
        return try {
            val response = api.getMediaCredits(mediaType = mediaType.apiValue, mediaId = mediaId)
            Result.Success(response.cast.sortedBy { it.order }.take(15).map { it.toDomain() })
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown error", status = 500)
        }
    }

    override suspend fun getRelatedMedia(mediaId: Int, mediaType: MediaType): Result<List<Movie>> {
        return try {
            val response = api.getSimilarMedia(mediaType = mediaType.apiValue, mediaId = mediaId)
            Result.Success(
                response.movies
                    .map { it.toDomain(mediaType = mediaType) }
                    .distinctBy { it.id }
                    .take(15)
            )
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown error", status = 500)
        }
    }
    
//    suspend fun clearCache() {
//        dao.clearAllMovies()
//    }
    
//    suspend fun getCachedMovieCount(): Int {
//        return dao.getMovieCount()
//    }
}
