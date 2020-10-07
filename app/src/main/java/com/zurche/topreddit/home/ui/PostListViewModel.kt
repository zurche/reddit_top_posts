package com.zurche.topreddit.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.zurche.topreddit.home.data.Resource
import com.zurche.topreddit.home.data.TopPostRepository
import kotlinx.coroutines.Dispatchers

class PostListViewModel(private val topPostRepository: TopPostRepository) : ViewModel() {

    fun getTopPosts() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = topPostRepository.getTopPosts()))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

}