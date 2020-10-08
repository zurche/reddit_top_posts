package com.zurche.topreddit.home.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.zurche.topreddit.home.data.TopPostRepository
import com.zurche.topreddit.home.data.remote.service.ChildrenData
import kotlinx.coroutines.flow.Flow

class PostListViewModel(topPostRepository: TopPostRepository) : ViewModel() {

    private val _pagedTopPosts = MutableLiveData<Flow<PagingData<ChildrenData>>>()
    val pagedTopPosts: LiveData<Flow<PagingData<ChildrenData>>>
        get() = _pagedTopPosts

    init {
        _pagedTopPosts.value = topPostRepository.getPagedTopPosts().cachedIn(viewModelScope)
    }

}