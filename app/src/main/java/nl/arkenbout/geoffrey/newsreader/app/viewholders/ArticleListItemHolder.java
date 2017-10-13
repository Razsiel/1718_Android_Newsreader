package nl.arkenbout.geoffrey.newsreader.app.viewholders;


import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import nl.arkenbout.geoffrey.newsreader.R;
import nl.arkenbout.geoffrey.newsreader.model.Article;
import nl.arkenbout.geoffrey.newsreader.model.User;

public class ArticleListItemHolder extends RecyclerView.ViewHolder {
    private final TextView titleView;
    private final TextView descriptionView;
    private final ImageView imageView;

    public ArticleListItemHolder(View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.article_image);
        titleView = itemView.findViewById(R.id.article_title);
        descriptionView = itemView.findViewById(R.id.article_summary);
    }

    public void fill(final Context context, final Article article) {
        // Image
        String url = article.getImage();
        Picasso.with(context)
                .load(url)
                .error(R.drawable.ic_broken_image_24dp)
                .placeholder(R.drawable.ic_image_24dp)
                .into(imageView, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        imageView.setImageTintList(null);
                        imageView.clearColorFilter();
                    }

                    @Override
                    public void onError() {
                        imageView.setColorFilter(ContextCompat.getColor(context, R.color.neutral));
                    }
                });

        // Title
        titleView.setText(article.getTitle());

        // Favorite Icon. Only shown when an authtoken (aka user logged in) is found
        if (User.isLoggedIn() && article.getIsLiked()) {
            // Draw a solid star based if article.isLiked
            titleView.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_star_24dp, 0);
        } else {
            // Do not draw a star if an article is not liked by the user or if the user is not logged in
            titleView.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
        }

        // Summary
        descriptionView.setText(article.getSummary());
    }
}
