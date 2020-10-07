package com.zurche.topreddit.home.data

import com.zurche.topreddit.home.data.remote.service.model.TopStoriesResponse
import retrofit2.Response

interface TopPostRepository {
    suspend fun getTopPosts(): Response<TopStoriesResponse>
}