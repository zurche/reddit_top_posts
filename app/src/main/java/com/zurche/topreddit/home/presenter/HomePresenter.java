package com.zurche.topreddit.home.presenter;

import android.util.Log;

import com.zurche.topreddit.home.HomeContract;
import com.zurche.topreddit.home.service.RedditService;
import com.zurche.topreddit.home.service.model.TopStoriesResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

/**
 * @author alejandro.zurcher
 * @since {insert app current main version here}
 */

public class HomePresenter implements HomeContract.Presenter {

    private static final String TAG = HomePresenter.class.getSimpleName();

    private static final String STORIES_LIMIT = "50";

    private HomeContract.View mHomeView;

    private RedditService mRedditService;

    public HomePresenter(HomeContract.View homeView) {
        mHomeView = homeView;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.reddit.com/")
                .addConverterFactory(MoshiConverterFactory.create())
                .build();

        mRedditService = retrofit.create(RedditService.class);
    }

    @Override
    public void retrieveTopPosts() {
        mHomeView.setRefreshingView();
        Call<TopStoriesResponse> getTopStoriesCall = mRedditService.listTopStories(STORIES_LIMIT);
        getTopStoriesCall.enqueue(new Callback<TopStoriesResponse>() {
            @Override
            public void onResponse(Call<TopStoriesResponse> call, Response<TopStoriesResponse> response) {
                Log.d(TAG, "Got stories " + response.body());
                mHomeView.onPopulatePostsList(response.body().getData().getChildren());
            }

            @Override
            public void onFailure(Call<TopStoriesResponse> call, Throwable t) {
                Log.e(TAG, "Failed to retrieve stories", t);
                mHomeView.onFailedToretrievePosts();
            }
        });


    }
}
