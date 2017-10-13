package nl.arkenbout.geoffrey.newsreader.app.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import java.util.ArrayList;

import nl.arkenbout.geoffrey.newsreader.R;
import nl.arkenbout.geoffrey.newsreader.app.adapter.NewsItemAdapter;
import nl.arkenbout.geoffrey.newsreader.app.listener.ListItemClickListener;
import nl.arkenbout.geoffrey.newsreader.model.Article;
import nl.arkenbout.geoffrey.newsreader.model.User;

public class MainActivity extends AppCompatActivity implements ListItemClickListener, View.OnClickListener {

    public static final String INTENT_ARTICLE = "intent_article";
    private final int scrollLenience = 10;
    private final int loginRequestCode = 100;
    private RecyclerView listView;
    private NewsItemAdapter adapter;
    private LinearLayoutManager verticalList;
    private ProgressBar progressBar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle drawerToggle;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        initRecyclerView();
        initToolbar();

        // Load initial items
        adapter.onLoadMore();
    }

    private void initRecyclerView() {
        // Prepare listView for recycling
        listView = (RecyclerView) findViewById(R.id.recyclerList);
        verticalList = new LinearLayoutManager(this);
        listView.setLayoutManager(verticalList);

        // Adapter with viewModel
        adapter = new NewsItemAdapter(this, new ArrayList<Article>(), progressBar);
        listView.setAdapter(adapter);

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

    private void initToolbar() {
        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Drawer
        drawer = (DrawerLayout) findViewById(R.id.drawer);
        drawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawerOpen, R.string.drawerClosed) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        drawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        // Drawer content
        if (TextUtils.isEmpty(User.getAuthtoken())) {
            loginButton = (Button) findViewById(R.id.login_button);
            loginButton.setOnClickListener(this);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_button:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivityForResult(intent, loginRequestCode);
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == loginRequestCode && resultCode == RESULT_OK) {
            loginButton.setVisibility(View.GONE);
            drawer.closeDrawers();
            adapter.refresh();
        }
    }
}
