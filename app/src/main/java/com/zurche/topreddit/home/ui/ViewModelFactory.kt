package com.zurche.topreddit.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zurche.topreddit.home.data.TopPostRepository
import com.zurche.topreddit.home.data.remote.TopPostRemoteDataSource

class ViewModelFactory(private val topPostRepository: TopPostRepository)
    : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PostListViewModel::class.java)) {
            return PostListViewModel(topPostRepository) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}
