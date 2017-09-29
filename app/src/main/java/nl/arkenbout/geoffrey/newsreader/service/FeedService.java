package nl.arkenbout.geoffrey.newsreader.service;


import java.util.List;

import nl.arkenbout.geoffrey.newsreader.model.Feed;
import retrofit2.Call;
import retrofit2.http.GET;

public interface FeedService {

    @GET
    Call<List<Feed>> getFeeds();
}
