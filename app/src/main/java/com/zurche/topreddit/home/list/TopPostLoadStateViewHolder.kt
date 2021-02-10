package com.zurche.topreddit.home.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.zurche.topreddit.R
import com.zurche.topreddit.databinding.TopPostLoadingStateFooterViewItemBinding

class TopPostLoadStateViewHolder(private val binding: TopPostLoadingStateFooterViewItemBinding,
                                 retry: () -> Unit)
    : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.retryButton.also {
            it.setOnClickListener {
                retry.invoke()
            }
        }
    }

    fun bind(loadState: LoadState) {
        if (loadState is LoadState.Error) {
            binding.errorMessage.text = loadState.error.localizedMessage
        }
        binding.loadingProgressBar.isVisible = loadState is LoadState.Loading
        binding.retryButton.isVisible = loadState !is LoadState.Loading
        binding.errorMessage.isVisible = loadState !is LoadState.Loading
    }

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): TopPostLoadStateViewHolder {
            val itemView = LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.top_post_loading_state_footer_view_item, parent, false)
            val binding = TopPostLoadingStateFooterViewItemBinding.bind(itemView)
            return TopPostLoadStateViewHolder(binding, retry)
        }
    }
}