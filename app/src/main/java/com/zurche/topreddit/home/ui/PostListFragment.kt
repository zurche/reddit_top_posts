package com.zurche.topreddit.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.zurche.topreddit.databinding.PostListFragmentBinding
import com.zurche.topreddit.home.data.remote.TopPostRemoteDataSource
import com.zurche.topreddit.home.list.TopPostLoadStateAdapter
import com.zurche.topreddit.home.list.TopPostsAdapter
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PostListFragment : Fragment() {

    private lateinit var binding: PostListFragmentBinding
    private lateinit var viewModel: PostListViewModel
    private lateinit var adapter: TopPostsAdapter
    private var loadTopPostsJob: Job? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = PostListFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val viewModelFactory = ViewModelFactory(TopPostRemoteDataSource())
        viewModel = ViewModelProvider(this, viewModelFactory)
                .get(PostListViewModel::class.java)

        adapter = TopPostsAdapter()

        binding.tryAgainButton.setOnClickListener { adapter.retry() }

        binding.postsList.layoutManager = LinearLayoutManager(requireContext())
        binding.postsList.adapter = adapter.withLoadStateHeaderAndFooter(
                header = TopPostLoadStateAdapter { adapter.retry() },
                footer = TopPostLoadStateAdapter { adapter.retry() }
        )

        adapter.addLoadStateListener { loadState ->
            binding.postsList.isVisible = loadState.source.refresh is LoadState.NotLoading
            binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
            binding.swwTv.isVisible = loadState.source.refresh is LoadState.Error
            binding.tryAgainButton.isVisible = loadState.source.refresh is LoadState.Error

            val errorState = loadState.source.append as? LoadState.Error
                    ?: loadState.source.prepend as? LoadState.Error
                    ?: loadState.append as? LoadState.Error
                    ?: loadState.prepend as? LoadState.Error
            errorState?.let {
                Toast.makeText(
                        requireContext(),
                        "\uD83D\uDE28 Wooops ${it.error}",
                        Toast.LENGTH_LONG
                ).show()
            }
        }

        loadTopPostsJob?.cancel()
        loadTopPostsJob = lifecycleScope.launch {
            viewModel.getPagedResults().collectLatest {
                adapter.submitData(it)
            }
        }
    }
}