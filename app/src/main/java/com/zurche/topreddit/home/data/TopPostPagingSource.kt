package com.zurche.topreddit.home.data

import android.util.Log
import androidx.paging.PagingSource
import com.zurche.topreddit.home.data.remote.service.ChildrenData
import com.zurche.topreddit.home.data.remote.service.RedditService
import retrofit2.HttpException
import java.io.IOException

class TopPostPagingSource(private val redditService: RedditService)
    : PagingSource<Int, ChildrenData>() {

    private var currentLastPost: String? = ""

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ChildrenData> {
        val currentPage = params.key ?: 1
        Log.d("Pagination", "Page Size: ${params.loadSize} § Current Page: $currentPage § Last After Tag: $currentLastPost")
        return try {
            val response = redditService.listTopStoriesAfter(params.loadSize, currentLastPost)
            val topPosts = response.body()?.data?.children
            currentLastPost = response.body()?.data?.after
            LoadResult.Page(
                    data = topPosts ?: emptyList(),
                    prevKey = if (currentPage == 1) null else currentPage - 1,
                    nextKey = if (topPosts.isNullOrEmpty()) null else currentPage + 1
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

}