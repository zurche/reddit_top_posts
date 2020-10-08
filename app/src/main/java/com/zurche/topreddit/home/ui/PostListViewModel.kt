package com.zurche.topreddit.home.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.zurche.topreddit.home.data.Resource
import com.zurche.topreddit.home.data.TopPostRepository
import com.zurche.topreddit.home.data.remote.service.ChildrenData
import com.zurche.topreddit.home.data.remote.service.TopStoriesResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

class PostListViewModel(private val topPostRepository: TopPostRepository) : ViewModel() {

    private val topPostList = MutableLiveData<Resource<List<ChildrenData>>>()

    fun getPagedResults() = topPostRepository.getPagedTopPosts().cachedIn(viewModelScope)

    private fun fetchTopPosts() {
        viewModelScope.launch {
            topPostList.postValue(Resource.loading(data = null))
            try {
                val topPosts = topPostRepository.getTopPosts().body() as TopStoriesResponse
                topPostList.postValue(Resource.success(data = topPosts.data.children))
            } catch (timeoutException: TimeoutException) {
                topPostList.postValue(Resource.cannotLoad(message = timeoutException.message
                        ?: "Timeout trying to get top posts!"))
            } catch (unknownHostException: UnknownHostException) {
                topPostList.postValue(Resource.cannotLoad(message = unknownHostException.message
                        ?: "Unknown host trying to get top posts!"))
            } catch (exception: Exception) {
                topPostList.postValue(Resource.error(data = null, message = exception.message
                        ?: "Error Occurred!"))
            }
        }
    }

    fun getTopPosts(): LiveData<Resource<List<ChildrenData>>> {
        return topPostList
    }
}