package com.antsyferov.impl.network

import androidx.paging.PagingSource
import androidx.paging.PagingState
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

class ExamplePagingSource @Inject constructor(
    val backend: AsteroidsApi,
) : PagingSource<Int, Asteroid>() {
    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, Asteroid> {
        return try {
            val nextPageNumber = params.key ?: 0
            val (start, end)  = getDateRange(nextPageNumber)
            val response = backend.getAsteroids(start, end)
            LoadResult.Page(
                data = response.asteroids.flatMap { it.value },
                prevKey = null,
                nextKey = nextPageNumber + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Asteroid>): Int? {
        // Try to find the page key of the closest page to anchorPosition from
        // either the prevKey or the nextKey; you need to handle nullability
        // here.
        //  * prevKey == null -> anchorPage is the first page.
        //  * nextKey == null -> anchorPage is the last page.
        //  * both prevKey and nextKey are null -> anchorPage is the
        //    initial page, so return null.
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    private val formatter = SimpleDateFormat("yyyy-MM-dd")
    fun getDateRange(page: Int): Pair<String, String> {
        val calendar = Calendar.getInstance()

        calendar.time = Date()

        calendar.add(Calendar.DAY_OF_YEAR, page * -7)
        val startDate = calendar.time

        calendar.add(Calendar.DAY_OF_YEAR, 7)
        val endDate = calendar.time

        return Pair(formatter.format(startDate), formatter.format(endDate))
    }
}