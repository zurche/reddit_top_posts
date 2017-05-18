package com.zurche.topreddit.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;

import com.squareup.picasso.Picasso;
import com.zurche.topreddit.R;
import com.zurche.topreddit.home.service.model.ChildrenData;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author alejandro.zurcher
 * @since {insert app current main version here}
 */

public class TopPostsAdapter extends RecyclerView.Adapter<TopPostViewHolder> {

    private List<ChildrenData> mTopPosts;

    private Context mContext;

    public TopPostsAdapter(List<ChildrenData> topPosts) {
        mTopPosts = topPosts;
    }

    @Override
    public TopPostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        mContext = parent.getContext();
        return new TopPostViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return mTopPosts.size();
    }

    @Override
    public void onBindViewHolder(TopPostViewHolder holder, int position) {
        holder.vTitle.setText(mTopPosts.get(position).getData().getTitle());

        String byAuthorString = mContext.getString(R.string.by, mTopPosts.get(position).getData().getAuthor());
        holder.vAuthor.setText(byAuthorString);

        String commentsCountString = mContext.getString(R.string.comments, mTopPosts.get(position).getData().getNum_comments());
        holder.vCommentsAmount.setText(commentsCountString);

        String postCreatingString = mContext.getString(R.string.created, getFormatedDate(position));
        holder.vEntryDate.setText(postCreatingString);

        final String thumbnailImageUrl = mTopPosts.get(position).getData().getThumbnail();
        setupThumbnail(holder, thumbnailImageUrl);
    }

    /**
     * Process the UNIX timestamp of the post creating date and return a format like "x hours"
     *
     * @param position current post element position being checked
     * @return hours since its creation
     */
    private long getFormatedDate(int position) {
        Date postDate = new Date(mTopPosts.get(position).getData().getCreated() * 1000);
        long elapsedTime = new Date().getTime() - postDate.getTime();
        return TimeUnit.MILLISECONDS.toHours(elapsedTime);
    }

    private void setupThumbnail(TopPostViewHolder holder, final String thumbnailImageUrl) {

        if (URLUtil.isValidUrl(thumbnailImageUrl)) {
            holder.vThumbnail.setVisibility(View.VISIBLE);

            Picasso.with(mContext)
                    .load(thumbnailImageUrl)
                    .resize(100, 100)
                    .centerCrop()
                    .into(holder.vThumbnail);

            holder.vThumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(thumbnailImageUrl));
                    mContext.startActivity(i);
                }
            });
        } else {
            holder.vThumbnail.setVisibility(View.GONE);
        }
    }

}
