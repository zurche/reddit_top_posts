package com.zurche.topreddit.home.list

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.zurche.topreddit.home.data.remote.service.ChildrenData

/**
 * @author alejandro.zurcher
 * @since {insert app current main version here}
 */
class TopPostsAdapter : PagingDataAdapter<ChildrenData, TopPostViewHolder>(POST_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopPostViewHolder {
        return TopPostViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: TopPostViewHolder, position: Int) {
        val topPost = getItem(position)
        if (topPost != null) {
            holder.bind(topPost.data)
        }
    }

    companion object {
        private val POST_COMPARATOR = object : DiffUtil.ItemCallback<ChildrenData>() {
            override fun areItemsTheSame(oldItem: ChildrenData, newItem: ChildrenData): Boolean =
                    oldItem.data.title == newItem.data.title

            override fun areContentsTheSame(oldItem: ChildrenData, newItem: ChildrenData): Boolean =
                    oldItem == newItem
        }
    }
}