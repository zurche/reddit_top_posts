package com.zurche.topreddit.home.view;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.zurche.topreddit.R;
import com.zurche.topreddit.home.HomeContract;
import com.zurche.topreddit.home.adapter.TopPostsAdapter;
import com.zurche.topreddit.home.presenter.HomePresenter;
import com.zurche.topreddit.home.service.model.ChildrenData;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity implements HomeContract.View {

    private HomePresenter mPresenter;

    @BindView(R.id.posts_list)
    RecyclerView vPostsList;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout vSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        vPostsList.setLayoutManager(mLayoutManager);

        mPresenter = new HomePresenter(this);

        vSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.retrieveTopPosts();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.retrieveTopPosts();
    }

    @Override
    public void onPopulatePostsList(List<ChildrenData> topPosts) {
        vSwipeRefreshLayout.setRefreshing(false);
        vPostsList.setVisibility(View.VISIBLE);

        TopPostsAdapter topPostsAdapter = new TopPostsAdapter(topPosts);
        vPostsList.setAdapter(topPostsAdapter);
    }

    @Override
    public void onFailedToretrievePosts() {
        vSwipeRefreshLayout.setRefreshing(false);

        Toast.makeText(this, "Failed to retrieve posts\nCheck device connectivity", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setRefreshingView() {
        vSwipeRefreshLayout.setRefreshing(true);
        vPostsList.setVisibility(View.GONE);
    }
}
