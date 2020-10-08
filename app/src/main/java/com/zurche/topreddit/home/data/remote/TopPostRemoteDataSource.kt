package com.zurche.topreddit.home.data.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.zurche.topreddit.home.data.TopPostPagingSource
import com.zurche.topreddit.home.data.TopPostRepository
import com.zurche.topreddit.home.data.remote.service.ChildrenData
import com.zurche.topreddit.home.data.remote.service.RedditService
import kotlinx.coroutines.flow.Flow
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


class TopPostRemoteDataSource : TopPostRepository {

    private val service: RedditService

    companion object {
        const val BASE_URL = "https://www.reddit.com/"
        const val LIMIT = 20
    }

    init {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS)
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()

        service = retrofit.create(RedditService::class.java)
    }

    override suspend fun getTopPosts() = service.listTopStories(LIMIT)

    override fun getPagedTopPosts(): Flow<PagingData<ChildrenData>> {
        return Pager(
                config = PagingConfig(pageSize = LIMIT, enablePlaceholders = false),
                pagingSourceFactory = { TopPostPagingSource(service) }
        ).flow
    }
}