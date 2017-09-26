package inholland.nl.newsreader;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import inholland.nl.newsreader.adapter.NewsItemAdapter;
import inholland.nl.newsreader.model.Article;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Prepare listview for recycling
        final RecyclerView listView = (RecyclerView) findViewById(R.id.recyclerList);
        final LinearLayoutManager verticalList = new LinearLayoutManager(this);
        listView.setLayoutManager(verticalList);

        // Adapter with viewmodel
        final NewsItemAdapter adapter = new NewsItemAdapter(this, new ArrayList<Article>(), listView);
        listView.setAdapter(adapter);
    }
}
