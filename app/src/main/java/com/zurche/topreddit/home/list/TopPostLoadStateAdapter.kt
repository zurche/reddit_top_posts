package com.zurche.topreddit.home.list

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter

class TopPostLoadStateAdapter(private val retry: () -> Unit) : LoadStateAdapter<TopPostLoadStateViewHolder>() {
    override fun onBindViewHolder(holder: TopPostLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): TopPostLoadStateViewHolder {
        return TopPostLoadStateViewHolder.create(parent, retry)
    }
}