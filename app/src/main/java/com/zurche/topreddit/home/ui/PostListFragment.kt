package com.zurche.topreddit.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.zurche.topreddit.R
import com.zurche.topreddit.home.data.remote.TopPostRemoteDataSource
import com.zurche.topreddit.home.list.TopPostLoadStateAdapter
import com.zurche.topreddit.home.list.TopPostsAdapter
import kotlinx.android.synthetic.main.post_list_fragment.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PostListFragment : Fragment() {

    private lateinit var viewModel: PostListViewModel
    private lateinit var adapter: TopPostsAdapter
    private var loadTopPostsJob: Job? = null

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
        posts_list.adapter = adapter.withLoadStateHeaderAndFooter(
                header = TopPostLoadStateAdapter { adapter.retry() },
                footer = TopPostLoadStateAdapter { adapter.retry() }
        )

        loadTopPostsJob?.cancel()
        loadTopPostsJob = lifecycleScope.launch {
            viewModel.getPagedResults().collectLatest {
                adapter.submitData(it)
            }
        }
    }
}