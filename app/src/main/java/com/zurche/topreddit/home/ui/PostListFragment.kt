package com.zurche.topreddit.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.zurche.topreddit.R
import com.zurche.topreddit.home.data.remote.service.TopStoriesResponse
import com.zurche.topreddit.home.data.Status
import com.zurche.topreddit.home.data.remote.TopPostRemoteDataSource
import kotlinx.android.synthetic.main.post_list_fragment.*

class PostListFragment : Fragment() {

    private lateinit var viewModel: PostListViewModel

    private lateinit var adapter: TopPostsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.post_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val viewModelFactory = ViewModelFactory(TopPostRemoteDataSource())
        viewModel = ViewModelProvider(this, viewModelFactory)
                .get(PostListViewModel::class.java)

        adapter = TopPostsAdapter()

        posts_list.layoutManager = LinearLayoutManager(requireContext())
        posts_list.adapter = adapter

        setupObservers()
    }

    private fun setupObservers() {
        viewModel.getTopPosts().observe(viewLifecycleOwner, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        posts_list.visibility = View.VISIBLE
                        progress_bar.visibility = View.GONE
                        resource.data?.let { topPosts ->
                            adapter.apply {
                                val updatedPosts = (topPosts.body() as TopStoriesResponse).data.children
                                updateTopPosts(updatedPosts)
                                notifyDataSetChanged()
                            }
                        }
                    }
                    Status.ERROR -> {
                        posts_list.visibility = View.VISIBLE
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                        progress_bar.visibility = View.GONE
                    }
                    Status.LOADING -> {
                        posts_list.visibility = View.GONE
                        progress_bar.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

}