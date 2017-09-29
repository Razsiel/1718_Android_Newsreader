package nl.arkenbout.geoffrey.newsreader.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;

import nl.arkenbout.geoffrey.newsreader.R;
import nl.arkenbout.geoffrey.newsreader.adapter.ListItemClickListener;
import nl.arkenbout.geoffrey.newsreader.adapter.NewsItemAdapter;
import nl.arkenbout.geoffrey.newsreader.model.Article;

public class MainActivity extends AppCompatActivity implements ListItemClickListener {

    public static final String INTENT_ARTICLE = "intent_article";
    private final int scrollLenience = 10;
    private RecyclerView listView;
    private NewsItemAdapter adapter;
    private LinearLayoutManager verticalList;
    private RecyclerView.OnScrollListener scrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Prepare listView for recycling
        listView = (RecyclerView) findViewById(R.id.recyclerList);
        verticalList = new LinearLayoutManager(this);
        listView.setLayoutManager(verticalList);

        // Adapter with viewModel
        adapter = new NewsItemAdapter(this, new ArrayList<Article>(), this);
        listView.setAdapter(adapter);

        adapter.onLoadMore();

        // Add scroll listener
        listView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // Get the amount of items created/loaded
                int totalItemCount = adapter.getItemCount();
                // The index of the last visible item
                int lastVisibleItem = verticalList.findLastVisibleItemPosition();
                if (totalItemCount <= (lastVisibleItem + scrollLenience)) {
                    // call collection method
                    adapter.onLoadMore();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_bar_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                adapter.refresh();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, ArticleDetailActivity.class);
        Article article = adapter.getItem(position);
        intent.putExtra(MainActivity.INTENT_ARTICLE, article);

        ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(this,
                new Pair<>(findViewById(R.id.article_image), ArticleDetailActivity.VIEW_NAME_ARTICLE_IMAGE),
                new Pair<>(findViewById(R.id.article_title), ArticleDetailActivity.VIEW_NAME_ARTICLE_TITLE),
                new Pair<>(findViewById(R.id.article_summary), ArticleDetailActivity.VIEW_NAME_ARTICLE_SUMMARY));

        startActivity(intent, activityOptions.toBundle());
    }
}
