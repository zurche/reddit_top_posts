package com.zurche.topreddit.home.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.zurche.topreddit.R
import com.zurche.topreddit.home.data.remote.service.ChildrenData
import com.zurche.topreddit.home.data.remote.service.NewsData
import kotlinx.android.synthetic.main.post_item.view.*
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

/**
 * @author alejandro.zurcher
 * @since {insert app current main version here}
 */
class TopPostsAdapter : RecyclerView.Adapter<TopPostsAdapter.TopPostViewHolder>() {

    private val topPosts: MutableList<ChildrenData> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopPostViewHolder {
        val itemView = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.post_item, parent, false)
        return TopPostViewHolder(itemView)
    }

    override fun getItemCount() = topPosts.size

    override fun onBindViewHolder(holder: TopPostViewHolder, position: Int) = holder.bind(topPosts[position].data)

    /**
     * Update the posts list.
     */
    fun updateTopPosts(updatedPosts: List<ChildrenData>) =
            topPosts.apply {
                clear()
                addAll(updatedPosts)
            }

    inner class TopPostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(newsData: NewsData) {
            itemView.apply {
                title.text = newsData.title
                author.text = context.getString(R.string.by, newsData.author)
                comments_amount.text = context.getString(R.string.comments, newsData.num_comments)
                entry_date.text = context.getString(R.string.created, getFormatedDate(newsData))

                if (URLUtil.isValidUrl(newsData.thumbnail)) {
                    thumbnail.visibility = View.VISIBLE
                    Picasso.get()
                            .load(newsData.thumbnail)
                            .resize(100, 100)
                            .centerCrop()
                            .into(thumbnail)
                } else {
                    thumbnail.visibility = View.GONE
                }
            }
        }

        /**
         * Process the UNIX timestamp of the post creating date and return a format like "x hours"
         *
         * @param newsData current post element position being checked
         * @return hours since its creation
         */
        private fun getFormatedDate(newsData: NewsData): Long {
            val postDate = Date(newsData.created * 1000)
            val elapsedTime = Date().time - postDate.time
            return TimeUnit.MILLISECONDS.toHours(elapsedTime)
        }
    }
}