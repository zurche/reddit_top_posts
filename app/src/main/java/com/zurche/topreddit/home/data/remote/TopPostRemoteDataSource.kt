package com.zurche.topreddit.home.data.remote

import com.zurche.topreddit.home.data.TopPostRepository
import com.zurche.topreddit.home.data.remote.service.RedditService
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class TopPostRemoteDataSource : TopPostRepository {

    private val service: RedditService

    companion object {
        const val BASE_URL = "https://www.reddit.com/"
        const val LIMIT = 50.toString()
    }

    init {
        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()

        service = retrofit.create(RedditService::class.java)
    }

    override suspend fun getTopPosts() = service.listTopStories(LIMIT)
}