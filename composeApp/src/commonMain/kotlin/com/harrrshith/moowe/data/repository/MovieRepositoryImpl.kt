package com.harrrshith.moowe.data.repository

import com.harrrshith.moowe.data.local.MooweDao
import com.harrrshith.moowe.data.remote.MooweApiHandler
import com.harrrshith.moowe.data.toDomain
import com.harrrshith.moowe.data.toEntity
import com.harrrshith.moowe.domain.model.Genre
import com.harrrshith.moowe.domain.model.Movie
import com.harrrshith.moowe.domain.repository.MovieRepository
import com.harrrshith.moowe.domain.utility.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class MovieRepositoryImpl(
    private val api: MooweApiHandler,
    private val dao: MooweDao
) : MovieRepository {

    override fun getTrendingMovies(): Flow<Result<List<Movie>>> = flow {
        val response = api.getTrendingMovies()
        val entities = response.movies.map { it.toEntity().copy(genre = Genre.TRENDING.id) }
        dao.insertMovies(entities)
        emitAll(
            dao.getMoviesByGenre(id = Genre.TRENDING.id)
                .map { list ->
                    Result.Success(
                        list.map { it.toDomain() }
                            .distinctBy { it.id }
                            .sortedWith(
                                compareByDescending<Movie> { it.popularity }
                                    .thenByDescending { it.voteAverage }
                            )
                    )
                }
        )
    }.catch { e ->
        emit(
            handleError(e as Exception) {
                dao.getMoviesByGenre(id = Genre.TRENDING.id)
                    .firstOrNull()
                    ?.map { it.toDomain() } ?: emptyList()
            } as Result.Success<List<Movie>>
        )
    }

    override fun getMoviesByGenre(genre: Genre): Flow<Result<List<Movie>>> = flow {
        val response = api.getMoviesByGenre(genreId = genre.id)
        val entities = response.movies.map { it.toEntity().copy(genre = genre.id) }
        dao.insertMovies(entities)
        // ✅ Emits live DB updates automatically
        emitAll(
            dao.getMoviesByGenre(id = genre.id)
                .map { list ->
                    Result.Success(
                        list.map { it.toDomain() }
                            .distinctBy { it.id }
                            .sortedWith(
                                compareByDescending<Movie> { it.popularity }
                                    .thenByDescending { it.voteAverage }
                            )
                    )
                }
        )
    }.catch { e ->
        emit(
            handleError(e as Exception) {
                dao.getMoviesByGenre(id = genre.id)
                    .firstOrNull()
                    ?.map { it.toDomain() } ?: emptyList()
            } as Result.Success<List<Movie>>
        )
    }.flowOn(Dispatchers.IO)

    private suspend fun handleError(
        exception: Exception,
        fallback: suspend () -> List<Movie>
    ): Result<List<Movie>> {
        return try {
            val cachedMovies = fallback()
            if (cachedMovies.isNotEmpty()) {
                Result.Success(cachedMovies)
            } else {
                Result.Error(exception.message ?: "No cached data available", Int.MAX_VALUE)
            }
        } catch (exception: Exception) {
            Result.Error(exception.message ?: "Unknown error occurred", Int.MAX_VALUE)
        }
    }

    // Additional methods for local database operations
    fun getMoviesFlow(): Flow<List<Movie>> {
        return dao.getAllMoviesFlow().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getMovieById(id: Int): Result<Movie> {
        return try {
            val movie = dao.getMovieById(movieId = id)
            if (movie != null) {
                Result.Success(movie.toDomain())
            } else {
                Result.Error("Movie not found", status = 404)
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown error", status = 500)
        }
    }
    
    suspend fun clearCache() {
        dao.clearAllMovies()
    }
    
    suspend fun getCachedMovieCount(): Int {
        return dao.getMovieCount()
    }
}