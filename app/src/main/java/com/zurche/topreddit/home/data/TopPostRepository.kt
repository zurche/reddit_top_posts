package com.zurche.topreddit.home.data

import androidx.paging.PagingData
import com.zurche.topreddit.home.data.remote.service.ChildrenData
import com.zurche.topreddit.home.data.remote.service.TopStoriesResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface TopPostRepository {
    fun getPagedTopPosts(): Flow<PagingData<ChildrenData>>
}