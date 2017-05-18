package com.zurche.topreddit.home.presenter;

import android.util.Log;

import com.zurche.topreddit.home.HomeContract;
import com.zurche.topreddit.home.service.RedditService;
import com.zurche.topreddit.home.service.model.ChildrenData;
import com.zurche.topreddit.home.service.model.GetTopStoriesResponse;
import com.zurche.topreddit.home.view.HomeActivity;

import java.io.IOException;

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
        Call<GetTopStoriesResponse> getTopStoriesCall = mRedditService.listTopStories(STORIES_LIMIT);
        getTopStoriesCall.enqueue(new Callback<GetTopStoriesResponse>() {
            @Override
            public void onResponse(Call<GetTopStoriesResponse> call, Response<GetTopStoriesResponse> response) {
                Log.d(TAG, "Got stories " + response.body());
                mHomeView.onPopulatePostsList(response.body().getData().getChildren());
            }

            @Override
            public void onFailure(Call<GetTopStoriesResponse> call, Throwable t) {
                Log.e(TAG, "Failed to retrieve stories", t);
                mHomeView.onFailedToretrievePosts();
            }
        });


    }
}
