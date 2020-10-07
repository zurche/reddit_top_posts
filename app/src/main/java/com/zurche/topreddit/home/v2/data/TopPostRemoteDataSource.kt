package com.zurche.topreddit.home.v2.data

import com.zurche.topreddit.home.service.RedditService
import com.zurche.topreddit.home.service.model.ChildrenData
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

    override fun getTopPosts(): MutableList<ChildrenData> {
        return arrayListOf()
    }
}