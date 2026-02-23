package com.harrrshith.moowe.data.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.flow.Flow

data class PageResult<T>(
    val items: List<T>,
    val currentPage: Int,
    val totalPages: Int,
)

class AppPagingSource<T : Any>(
    private val startPage: Int = 1,
    private val fetchPage: suspend (page: Int, pageSize: Int) -> PageResult<T>,
) : PagingSource<Int, T>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        return try {
            val page = params.key ?: startPage
            val response = fetchPage(page, params.loadSize)
            val prevKey = if (page == startPage) null else page - 1
            val nextKey = if (response.currentPage >= response.totalPages) null else page + 1

            LoadResult.Page(
                data = response.items,
                prevKey = prevKey,
                nextKey = nextKey,
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val anchorPage = state.closestPageToPosition(anchorPosition) ?: return null
        return anchorPage.prevKey?.plus(1) ?: anchorPage.nextKey?.minus(1)
    }
}

fun <T : Any> createPagerFlow(
    pageSize: Int = 20,
    startPage: Int = 1,
    prefetchDistance: Int = pageSize / 2,
    enablePlaceholders: Boolean = false,
    fetchPage: suspend (page: Int, pageSize: Int) -> PageResult<T>,
): Flow<PagingData<T>> {
    return Pager(
        config = PagingConfig(
            pageSize = pageSize,
            prefetchDistance = prefetchDistance,
            enablePlaceholders = enablePlaceholders,
        ),
        pagingSourceFactory = {
            AppPagingSource(
                startPage = startPage,
                fetchPage = fetchPage,
            )
        }
    ).flow
}
