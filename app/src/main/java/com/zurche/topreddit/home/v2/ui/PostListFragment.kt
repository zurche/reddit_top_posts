package com.zurche.topreddit.home.v2.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.zurche.topreddit.R
import com.zurche.topreddit.home.adapter.TopPostsAdapter
import kotlinx.android.synthetic.main.activity_home.*

class PostListFragment : Fragment() {

    private lateinit var viewModel: PostListViewModel

    private lateinit var adapter: TopPostsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.post_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PostListViewModel::class.java)

        adapter = TopPostsAdapter()

        posts_list.layoutManager = LinearLayoutManager(requireContext())
        posts_list.adapter = adapter
    }

}