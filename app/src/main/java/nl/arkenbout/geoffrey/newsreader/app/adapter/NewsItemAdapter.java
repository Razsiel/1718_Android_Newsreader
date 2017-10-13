package nl.arkenbout.geoffrey.newsreader.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.List;

import nl.arkenbout.geoffrey.newsreader.R;
import nl.arkenbout.geoffrey.newsreader.app.listener.ListItemClickListener;
import nl.arkenbout.geoffrey.newsreader.app.listener.OnLoadMoreListener;
import nl.arkenbout.geoffrey.newsreader.app.viewholders.NewsItemViewHolder;
import nl.arkenbout.geoffrey.newsreader.model.Article;
import nl.arkenbout.geoffrey.newsreader.model.User;
import nl.arkenbout.geoffrey.newsreader.rest.RestClient;
import nl.arkenbout.geoffrey.newsreader.rest.model.ArticleResult;
import nl.arkenbout.geoffrey.newsreader.rest.service.ArticleService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NewsItemAdapter extends RecyclerView.Adapter<NewsItemViewHolder> implements OnLoadMoreListener, Callback<ArticleResult> {

    // Property fields
    private final ProgressBar progressBar;
    private final Context context;
    private final LayoutInflater layoutInflater;
    private final List<Article> items;
    private final ListItemClickListener listItemClickListener;
    private final ArticleService articleService;
    // User experience settings
    private final int loadingThresholdMs = 100;
    // Flag fields
    private int nextId = -1;
    private boolean loading;

    public NewsItemAdapter(Context context, List<Article> items, ProgressBar progressBar) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.items = items;
        this.listItemClickListener = (ListItemClickListener) context;
        this.progressBar = progressBar;

        RestClient restClient = new RestClient();
        articleService = restClient.createArticleService();
    }

    public Article getItem(int position) {
        return items.get(position);
    }

    public void refresh() {
        this.items.clear();
        nextId = -1;
        onLoadMore();
        notifyDataSetChanged();
    }

    @Override
    public NewsItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create the view and viewholder
        View view = layoutInflater.inflate(R.layout.news_list_item, parent, false);
        return new NewsItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final NewsItemViewHolder holder, int position) {
        // get model data from list based on position loaded
        final Article article = getItem(position);

        // Fill view holder
        holder.fill(context, article);

        // click handler for list item
        final int clickedItem = holder.getAdapterPosition();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listItemClickListener.onItemClick(clickedItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onLoadMore() {
        // add new item for continuous scrolling
        // load from backend here
        if (loading) return;
        loading = true;
        // Show the loading icon if the action takes longer than 'loadingThresholdMs'
        progressBar.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (loading)
                    progressBar.setVisibility(View.VISIBLE);
            }
        }, loadingThresholdMs);
        if (nextId < 0) {
            articleService.getArticles(User.getAuthtoken(), null, null, null, null).enqueue(this);
        } else {
            articleService.getArticle(User.getAuthtoken(), nextId, 20, null, null, null).enqueue(this);
        }
    }

    @Override
    public void onResponse(Call<ArticleResult> call, Response<ArticleResult> response) {
        if (response.isSuccessful() && response.body() != null) {
            ArticleResult result = response.body();
            nextId = result.getNextId();
            // Fill the list of items
            for (Article article : result.getResults()) {
                items.add(article);
            }
            notifyDataSetChanged();
        }
        clearRequest();
    }

    @Override
    public void onFailure(Call<ArticleResult> call, Throwable t) {
        // log failure here
        Log.e(this.getClass().toString().toUpperCase(), "Tried to connect, but something went wrong", t);
        clearRequest();
        //TODO: Show message here
    }

    private void clearRequest() {
        loading = false;
        // Hide the progressbar
        progressBar.setVisibility(View.GONE);
    }


}
