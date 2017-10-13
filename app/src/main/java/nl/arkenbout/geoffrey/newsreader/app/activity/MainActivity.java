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
import nl.arkenbout.geoffrey.newsreader.app.adapter.ArticleListItemAdapter;
import nl.arkenbout.geoffrey.newsreader.app.listener.ListItemClickListener;
import nl.arkenbout.geoffrey.newsreader.model.Article;
import nl.arkenbout.geoffrey.newsreader.model.User;

public class MainActivity extends AppCompatActivity implements ListItemClickListener, View.OnClickListener {

    public static final String INTENT_ARTICLE = "intent_article";
    private final int scrollLenience = 10;
    private final int loginRequestCode = 100;
    private final int detailRequestCode = 200;

    private RecyclerView listView;
    private ArticleListItemAdapter adapter;
    private LinearLayoutManager verticalList;

    private ProgressBar progressBar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle drawerToggle;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressbar);

        initRecyclerView();
        initToolbar();

        // Load initial items
        adapter.reload();
    }

    private void initRecyclerView() {
        // Prepare listView for recycling
        listView = findViewById(R.id.recyclerList);
        verticalList = new LinearLayoutManager(this);
        listView.setLayoutManager(verticalList);

        // Adapter with viewModel
        adapter = new ArticleListItemAdapter(this, new ArrayList<Article>(), progressBar);
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
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Drawer
        drawer = findViewById(R.id.drawer);
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
            loginButton = findViewById(R.id.login_button);
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
                adapter.reload();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this, ArticleDetailActivity.class);

        // Setup UI animation
        ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(this,
                new Pair<>(view.findViewById(R.id.article_image), ArticleDetailActivity.VIEW_NAME_ARTICLE_IMAGE),
                new Pair<>(view.findViewById(R.id.article_title), ArticleDetailActivity.VIEW_NAME_ARTICLE_TITLE),
                new Pair<>(view.findViewById(R.id.article_summary), ArticleDetailActivity.VIEW_NAME_ARTICLE_SUMMARY));

        // Pass data into intent
        Article article = adapter.getItem(position);
        intent.putExtra(MainActivity.INTENT_ARTICLE, article);

        startActivityForResult(intent, detailRequestCode, activityOptions.toBundle());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_button:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivityForResult(intent, loginRequestCode);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            // called from the login activity result
            case loginRequestCode:
                if (resultCode == RESULT_OK) {
                    onLoginActivityResult();
                }
                break;
            // called from the article detail activity result
            case detailRequestCode:
                if (resultCode == RESULT_OK) {
                    onDetailActivityResult(data);
                }
                break;
            default:
                break;
        }
    }

    private void onLoginActivityResult() {
        loginButton.setVisibility(View.GONE);
        drawer.closeDrawers();
        adapter.reload();
    }

    private void onDetailActivityResult(Intent data) {
        Article article = data.getParcelableExtra(INTENT_ARTICLE);

        // data could not be parsed
        if (article == null) return;

        adapter.updateArticle(article);
        adapter.refresh();
    }
}
