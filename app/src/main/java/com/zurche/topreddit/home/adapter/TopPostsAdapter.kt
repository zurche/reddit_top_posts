package com.zurche.topreddit.home.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.zurche.topreddit.R
import com.zurche.topreddit.home.service.model.ChildrenData
import kotlinx.android.synthetic.main.post_item.view.*
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author alejandro.zurcher
 * @since {insert app current main version here}
 */
class TopPostsAdapter(private val topPosts: List<ChildrenData>)
    : RecyclerView.Adapter<TopPostsAdapter.TopPostViewHolder>() {
    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopPostViewHolder {
        val itemView = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.post_item, parent, false)
        context = parent.context
        return TopPostViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return topPosts.size
    }

    override fun onBindViewHolder(holder: TopPostViewHolder, position: Int) {
        holder.title.text = topPosts[position].data.title
        val byAuthorString = context!!.getString(R.string.by, topPosts[position].data.author)
        holder.author.text = byAuthorString
        val commentsCountString = context!!.getString(R.string.comments, topPosts[position].data.num_comments)
        holder.commentsAmount.text = commentsCountString
        val postCreatingString = context!!.getString(R.string.created, getFormatedDate(position))
        holder.entryDate.text = postCreatingString
        val thumbnailImageUrl = topPosts[position].data.thumbnail
        setupThumbnail(holder, thumbnailImageUrl)
    }

    /**
     * Process the UNIX timestamp of the post creating date and return a format like "x hours"
     *
     * @param position current post element position being checked
     * @return hours since its creation
     */
    private fun getFormatedDate(position: Int): Long {
        val postDate = Date(topPosts[position].data.created * 1000)
        val elapsedTime = Date().time - postDate.time
        return TimeUnit.MILLISECONDS.toHours(elapsedTime)
    }

    private fun setupThumbnail(holder: TopPostViewHolder, thumbnailImageUrl: String) {
        if (URLUtil.isValidUrl(thumbnailImageUrl)) {
            holder.thumbnail.visibility = View.VISIBLE
            Picasso.get()
                    .load(thumbnailImageUrl)
                    .resize(100, 100)
                    .centerCrop()
                    .into(holder.thumbnail)
            holder.thumbnail.setOnClickListener {
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(thumbnailImageUrl)
                context!!.startActivity(i)
            }
        } else {
            holder.thumbnail.visibility = View.GONE
        }
    }

    inner class TopPostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.title
        var author: TextView = itemView.author
        var entryDate: TextView = itemView.entry_date
        var commentsAmount: TextView = itemView.comments_amount
        var thumbnail: ImageView = itemView.thumbnail
    }
}