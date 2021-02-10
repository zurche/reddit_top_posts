package com.zurche.topreddit.home.list

import android.view.LayoutInflater
import android.view.ViewGroup
import android.webkit.URLUtil
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.zurche.topreddit.R
import com.zurche.topreddit.databinding.PostItemBinding
import com.zurche.topreddit.home.data.remote.service.NewsData
import java.util.*
import java.util.concurrent.TimeUnit

class TopPostViewHolder(private val binding: PostItemBinding)
    : RecyclerView.ViewHolder(binding.root) {

    fun bind(newsData: NewsData) {
        binding.apply {
            title.text = newsData.title
            author.text = binding.root.resources.getString(R.string.by, newsData.author)
            commentsAmount.text = binding.root.resources.getString(R.string.comments, newsData.num_comments)
            entryDate.text = binding.root.resources.getString(R.string.created, getFormatedDate(newsData))

            val imageUrlIsValid = URLUtil.isValidUrl(newsData.thumbnail)
            thumbnail.isVisible = imageUrlIsValid
            if (imageUrlIsValid) {
                Picasso.get()
                        .load(newsData.thumbnail)
                        .resize(100, 100)
                        .centerCrop()
                        .into(thumbnail)
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

    companion object {
        fun create(parent: ViewGroup): TopPostViewHolder {
            val itemView = LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.post_item, parent, false)
            val binding = PostItemBinding.bind(itemView)
            return TopPostViewHolder(binding)
        }
    }
}

