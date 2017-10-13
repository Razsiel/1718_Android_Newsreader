package nl.arkenbout.geoffrey.newsreader.app.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import nl.arkenbout.geoffrey.newsreader.R;
import nl.arkenbout.geoffrey.newsreader.model.Article;
import nl.arkenbout.geoffrey.newsreader.model.User;
import nl.arkenbout.geoffrey.newsreader.rest.RestClient;
import nl.arkenbout.geoffrey.newsreader.rest.service.ArticleService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArticleDetailActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String VIEW_NAME_ARTICLE_IMAGE = "view_name_article_image";
    public static final String VIEW_NAME_ARTICLE_TITLE = "view_name_article_title";
    public static final String VIEW_NAME_ARTICLE_SUMMARY = "view_name_article_summary";

    private ToggleButton likeButton;
    private ImageView imageView;
    private TextView publishView;
    private TextView titleView;
    private TextView summaryView;

    private Article article;
    private ArticleService articleService;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        context = this;

        // setup services
        RestClient rest = new RestClient();
        articleService = rest.createArticleService();

        // setup views
        initViews();

        // Get data from intent passed by mainactivity
        Intent intent = getIntent();
        article = intent.getParcelableExtra(MainActivity.INTENT_ARTICLE);

        if (article == null) return;

        // fill views
        String url = article.getImage();
        Picasso.with(this).load(url).into(imageView);
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault());
        publishView.setText(df.format(article.getPublishDate()));
        titleView.setText(String.format(Locale.getDefault(), "%s", article.getTitle()));
        summaryView.setText(String.format(Locale.getDefault(), "%s", article.getSummary()));
        likeButton.setChecked(article.getIsLiked());
    }

    private void initViews() {
        // Get view elements
        imageView = findViewById(R.id.article_image);
        publishView = findViewById(R.id.article_publish_date);
        titleView = findViewById(R.id.article_title);
        summaryView = findViewById(R.id.article_summary);

        // read more Button
        Button readMoreBtn = findViewById(R.id.read_more_btn);
        readMoreBtn.setOnClickListener(this);

        // like Button
        likeButton = findViewById(R.id.like_btn);
        likeButton.setOnClickListener(this);
        // hide the like button if the user is not logged in
        if (User.isLoggedIn()) {
            likeButton.setVisibility(View.VISIBLE);
        }

        // Set transition data
        imageView.setTransitionName(VIEW_NAME_ARTICLE_IMAGE);
        titleView.setTransitionName(VIEW_NAME_ARTICLE_TITLE);
        summaryView.setTransitionName(VIEW_NAME_ARTICLE_SUMMARY);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_bar_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Actionbar click handler
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share:
                share();
                return true;
            case android.R.id.home:
                setupDataPassback();
                supportFinishAfterTransition();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Activity UI views click handler
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.read_more_btn:
                openArticle();
                break;
            case R.id.like_btn:
                likeArticle();
                break;
            default:
                break;
        }
    }

    private void setupDataPassback() {
        Intent backIntent = new Intent();
        // Pass the article back to the view that wants a result.
        // It would technically be cleaner to only pass relevant (changed) data instead of a copy of a parcel (which is a deep copy of the object)
        backIntent.putExtra(MainActivity.INTENT_ARTICLE, article);
        setResult(RESULT_OK, backIntent);
    }

    private void likeArticle() {
        if (!article.getIsLiked()) {
            articleService.putLikeArticle(User.getAuthtoken(), article.getId()).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    Toast.makeText(context, getResources().getString(R.string.toast_article_liked), Toast.LENGTH_SHORT).show();
                    // Set local copy data
                    article.setIsLiked(true);
                    // Manually update ui, just in case
                    likeButton.setChecked(true);
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    Toast.makeText(context, getResources().getString(R.string.toast_something_went_wrong), Toast.LENGTH_SHORT).show();
                    // Set the checked state forcefully to false since something went wrong
                    likeButton.setChecked(false);
                }
            });
        } else {
            articleService.deleteLikeArticle(User.getAuthtoken(), article.getId()).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    Toast.makeText(context, getResources().getString(R.string.toast_article_unliked), Toast.LENGTH_SHORT).show();
                    // Set local copy data
                    article.setIsLiked(false);
                    // Manually update ui, just in case
                    likeButton.setChecked(false);
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    Toast.makeText(context, getResources().getString(R.string.toast_something_went_wrong), Toast.LENGTH_SHORT).show();
                    // Set the checked state forcefully to false since something went wrong
                    likeButton.setChecked(true);
                }
            });
        }
    }

    public void openArticle() {
        Intent openUrlIntent = new Intent();
        openUrlIntent.setAction(Intent.ACTION_VIEW);
        openUrlIntent.setData(Uri.parse(article.getUrl()));
        startActivity(openUrlIntent);
    }

    private void share() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, article.getTitle());
        sendIntent.putExtra(Intent.EXTRA_TEXT, article.getSummary());
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.refresh)));
    }
}
