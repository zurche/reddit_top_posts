package com.zurche.topreddit.home.v2.data

import com.zurche.topreddit.home.service.model.ChildrenData

interface TopPostRepository {
    fun getTopPosts(): MutableList<ChildrenData>
}