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
import com.zurche.topreddit.home.data.Status
import com.zurche.topreddit.home.data.remote.TopPostRemoteDataSource
import com.zurche.topreddit.home.data.remote.service.ChildrenData
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
        viewModel.getTopPosts().observe(viewLifecycleOwner, Observer { topPostsListResource ->
            topPostsListResource?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> showTopPosts(topPostsListResource.data)
                    Status.ERROR -> showSomethingWentWrongMessage(topPostsListResource.message)
                    Status.LOADING -> showLoading()
                    Status.CANNOT_LOAD -> showLoadingIssuesMessage()
                }
            }
        })
    }

    private fun showLoadingIssuesMessage() {
        posts_list.visibility = View.GONE
        progress_bar.visibility = View.GONE
        cannot_load_message.visibility = View.VISIBLE
    }

    private fun showLoading() {
        posts_list.visibility = View.GONE
        progress_bar.visibility = View.VISIBLE
        cannot_load_message.visibility = View.GONE
    }

    private fun showSomethingWentWrongMessage(failureMessage: String?) {
        posts_list.visibility = View.VISIBLE
        progress_bar.visibility = View.GONE
        cannot_load_message.visibility = View.GONE
        Toast.makeText(requireContext(), failureMessage, Toast.LENGTH_LONG).show()
    }

    private fun showTopPosts(topPostsList: List<ChildrenData>?) {
        posts_list.visibility = View.VISIBLE
        progress_bar.visibility = View.GONE
        cannot_load_message.visibility = View.GONE
        topPostsList?.let {
            adapter.apply {
                updateTopPosts(it)
                notifyDataSetChanged()
            }
        }
    }

}