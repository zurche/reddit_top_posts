package com.zurche.topreddit.home.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lb.auto_fit_textview.AutoResizeTextView;
import com.zurche.topreddit.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author alejandro.zurcher
 * @since {insert app current main version here}
 */
class TopPostViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.title)
    AutoResizeTextView vTitle;

    @BindView(R.id.author)
    TextView vAuthor;

    @BindView(R.id.entry_date)
    TextView vEntryDate;

    @BindView(R.id.comments_amount)
    TextView vCommentsAmount;

    @BindView(R.id.thumbnail)
    ImageView vThumbnail;

    TopPostViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }
}
