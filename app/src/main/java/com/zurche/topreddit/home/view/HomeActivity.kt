package com.zurche.topreddit.home.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.zurche.topreddit.R
import com.zurche.topreddit.home.HomeContract
import com.zurche.topreddit.home.adapter.TopPostsAdapter
import com.zurche.topreddit.home.presenter.HomePresenter
import com.zurche.topreddit.home.service.model.ChildrenData
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity(), HomeContract.View {

    private var mPresenter: HomePresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val mLayoutManager = LinearLayoutManager(this)
        posts_list.layoutManager = mLayoutManager
        mPresenter = HomePresenter(this)
    }

    override fun onResume() {
        super.onResume()
        mPresenter!!.retrieveTopPosts()
    }

    override fun onPopulatePostsList(topPosts: List<ChildrenData>) {
        posts_list.visibility = View.VISIBLE
        val topPostsAdapter = TopPostsAdapter(topPosts)
        posts_list.adapter = topPostsAdapter
    }

    override fun onFailedToretrievePosts() =
            Toast.makeText(this, "Failed to retrieve posts\nCheck device connectivity", Toast.LENGTH_SHORT).show()


    override fun setRefreshingView() {
        posts_list.visibility = View.GONE
    }
}