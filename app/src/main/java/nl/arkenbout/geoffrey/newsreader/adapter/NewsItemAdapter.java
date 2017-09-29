package nl.arkenbout.geoffrey.newsreader.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Random;

import nl.arkenbout.geoffrey.newsreader.R;
import nl.arkenbout.geoffrey.newsreader.model.Article;
import nl.arkenbout.geoffrey.newsreader.model.User;
import nl.arkenbout.geoffrey.newsreader.service.ArticleService;
import nl.arkenbout.geoffrey.newsreader.service.responses.ArticleResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class NewsItemAdapter extends RecyclerView.Adapter<NewsItemAdapter.ArticleListItemViewHolder> implements OnLoadMoreListener, Callback<ArticleResult> {

    private final Random rng = new Random();
    private LayoutInflater layoutInflater;
    private List<Article> items;
    private int nextId = -1;
    private boolean loading;

    private Context context;

    private ListItemClickListener listItemClickListener;

    private ArticleService articleService;

    public NewsItemAdapter(Context context, List<Article> items, ListItemClickListener listItemClickListener) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.items = items;
        this.listItemClickListener = listItemClickListener;

        // Custom converter for dates since backend gives non-timezone'd dates which the GSON parser needs
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://inhollandbackend.azurewebsites.net/api/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        articleService = retrofit.create(ArticleService.class);
    }

    public Article getItem(int position) {
        return items.get(position);
    }

    public void refresh() {
        this.items.clear();
        nextId = -1;
        onLoadMore();
    }

    @Override
    public ArticleListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create the view and viewholder
        View view = layoutInflater.inflate(R.layout.news_list_item, parent, false);
        return new ArticleListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ArticleListItemViewHolder holder, int position) {
        // get model data from list based on position loaded
        Article article = getItem(position);
        // Fill view data
        int color = Color.argb(255, rng.nextInt(256), rng.nextInt(256), rng.nextInt(256));
        holder.imageView.setBackgroundColor(color);

        String url = article.getImage();
        // Load data into view
        Picasso.with(context).load(url).into(holder.imageView);
        holder.titleView.setText(article.getTitle());
        holder.descriptionView.setText(article.getSummary());

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
        if (nextId < 0) {
            articleService.getArticles(User.getAuthtoken(), null, null, null, null).enqueue(this);
        } else {
            articleService.getArticle(User.getAuthtoken(), nextId, null, null, null, null).enqueue(this);
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
        }
        notifyDataSetChanged();
        loading = false;
    }

    @Override
    public void onFailure(Call<ArticleResult> call, Throwable t) {
        // log failure here
        Log.e(this.getClass().toString(), "Tried to connect, but something went wrong", t);
        loading = false;
        //TODO: Show message here
    }

    // Viewholder / viewmodel
    static class ArticleListItemViewHolder extends RecyclerView.ViewHolder {
        public final TextView titleView;
        public final TextView descriptionView;
        public final ImageView imageView;

        ArticleListItemViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.article_image);
            titleView = itemView.findViewById(R.id.article_title);
            descriptionView = itemView.findViewById(R.id.article_summary);
        }
    }
}
