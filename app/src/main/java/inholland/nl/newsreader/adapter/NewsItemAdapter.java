package inholland.nl.newsreader.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

import inholland.nl.newsreader.R;
import inholland.nl.newsreader.model.Article;


public class NewsItemAdapter extends RecyclerView.Adapter<NewsItemAdapter.ArticleListItemViewHolder> {

    private final Random rng = new Random();
    private LayoutInflater layoutInflater;
    private List<Article> items;
    private NewsItemAdapter onLoadMoreListener;

    private int itemLenience = 3;

    public NewsItemAdapter(Context context, List<Article> items, RecyclerView view) {
        layoutInflater = LayoutInflater.from(context);
        this.items = items;
        onLoadMoreListener = this;

        // Check if the view has a scrollable layout manager (linearlayout)
        if (view.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager listView = (LinearLayoutManager) view.getLayoutManager();
            // Add Scroll listener
            view.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    // Get the amount of items created/loaded
                    int totalItemCount = listView.getItemCount();
                    // The index of the last visible item
                    int lastVisibleItem = listView.findLastVisibleItemPosition();
                    if (totalItemCount <= (lastVisibleItem + itemLenience)) {
                        // call collection method
                        onLoadMoreListener.onLoadMore();
                    }
                }
            });
        }
    }

    @Override
    public ArticleListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create the view and viewholder
        View view = layoutInflater.inflate(R.layout.news_list_item, parent, false);
        return new ArticleListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ArticleListItemViewHolder holder, int position) {
        // get model data from list based on position loaded
        Article article = items.get(position);
        // Fill view data
        int color = (128 + rng.nextInt(64)) + (128 + rng.nextInt(64)) + (128 + rng.nextInt(64));
        holder.imageView.setBackgroundColor(color);
        holder.imageView.setImageURI(Uri.parse(article.getUrl()));
        holder.titleView.setText(article.getTitle());
        holder.descriptionView.setText(article.getSummary());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private void onLoadMore() {
        // add new item for continuous scrolling
        // load from backend here
        this.items.add(new Article());
        notifyDataSetChanged();
    }

    /*
    Viewholder / viewmodel
     */
    static class ArticleListItemViewHolder extends RecyclerView.ViewHolder {
        public final TextView titleView;
        public final TextView descriptionView;
        public final ImageView imageView;

        ArticleListItemViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_image);
            titleView = itemView.findViewById(R.id.item_title);
            descriptionView = itemView.findViewById(R.id.item_description);
        }
    }
}
