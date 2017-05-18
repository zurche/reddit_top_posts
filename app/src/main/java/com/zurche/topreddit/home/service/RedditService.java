package com.zurche.topreddit.home.service;

import com.zurche.topreddit.home.service.model.GetTopStoriesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author alejandro.zurcher
 * @since {insert app current main version here}
 */

public interface RedditService {
    @GET("top.json")
    Call<GetTopStoriesResponse> listTopStories(@Query("limit") String limit);
}
