package nl.arkenbout.geoffrey.newsreader.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import nl.arkenbout.geoffrey.newsreader.R;
import nl.arkenbout.geoffrey.newsreader.model.Article;

public class ArticleDetailActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String VIEW_NAME_ARTICLE_IMAGE = "view_name_article_image";
    public static final String VIEW_NAME_ARTICLE_TITLE = "view_name_article_title";
    public static final String VIEW_NAME_ARTICLE_SUMMARY = "view_name_article_summary";

    private Article article;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        // Get data from intent passed by mainactivity
        Intent intent = getIntent();
        article = intent.getParcelableExtra(MainActivity.INTENT_ARTICLE);

        if (article == null) return;
        // Get view elements
        ImageView imageView = (ImageView) findViewById(R.id.article_image);
        TextView publishView = (TextView) findViewById(R.id.article_publish_date);
        TextView titleView = (TextView) findViewById(R.id.article_title);
        TextView summaryView = (TextView) findViewById(R.id.article_summary);

        Button readMoreBtn = (Button) findViewById(R.id.read_more_btn);
        readMoreBtn.setOnClickListener(this);

        // Set transition data
        imageView.setTransitionName(VIEW_NAME_ARTICLE_IMAGE);
        titleView.setTransitionName(VIEW_NAME_ARTICLE_TITLE);
        summaryView.setTransitionName(VIEW_NAME_ARTICLE_SUMMARY);

        // Fill view
        String url = article.getImage();
        Picasso.with(this).load(url).into(imageView);
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault());
        publishView.setText(df.format(article.getPublishDate()));
        titleView.setText(String.format(Locale.getDefault(), "%s", article.getTitle()));
        summaryView.setText(String.format(Locale.getDefault(), "%s", article.getSummary()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_bar_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share:
                share();
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void share() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, article.getTitle());
        sendIntent.putExtra(Intent.EXTRA_TEXT, article.getSummary());
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.refresh)));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.read_more_btn:
                openArticle();
            default:
                break;
        }
    }

    public void openArticle() {
        Intent openUrlIntent = new Intent();
        openUrlIntent.setAction(Intent.ACTION_VIEW);
        openUrlIntent.setData(Uri.parse(article.getUrl()));
        startActivity(openUrlIntent);
    }
}
