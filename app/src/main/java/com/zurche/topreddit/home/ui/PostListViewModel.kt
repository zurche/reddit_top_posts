package com.zurche.topreddit.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.zurche.topreddit.home.data.TopPostRepository

class PostListViewModel(private val topPostRepository: TopPostRepository) : ViewModel() {

    fun getPagedResults() = topPostRepository.getPagedTopPosts().cachedIn(viewModelScope)
}