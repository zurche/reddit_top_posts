package com.zurche.topreddit.home.data.remote.service

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author alejandro.zurcher
 * @since {insert app current main version here}
 */
interface RedditService {

    @GET("top.json")
    suspend fun listTopStories(@Query("limit") limit: String): Response<TopStoriesResponse>

}