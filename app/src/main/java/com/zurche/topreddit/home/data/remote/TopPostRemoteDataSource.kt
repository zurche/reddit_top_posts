package com.zurche.topreddit.home.data.remote

import com.zurche.topreddit.home.data.TopPostRepository
import com.zurche.topreddit.home.data.remote.service.RedditService
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class TopPostRemoteDataSource : TopPostRepository {

    private val service: RedditService

    init {
        val retrofit = Retrofit.Builder()
                .baseUrl("https://www.reddit.com/")
                .addConverterFactory(MoshiConverterFactory.create())
                .build()

        service = retrofit.create(RedditService::class.java)
    }

    override suspend fun getTopPosts() = service.listTopStoriesV2("50")
}