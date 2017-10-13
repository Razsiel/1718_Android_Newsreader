package nl.arkenbout.geoffrey.newsreader.rest;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import nl.arkenbout.geoffrey.newsreader.rest.service.ArticleService;
import nl.arkenbout.geoffrey.newsreader.rest.service.UserService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {

    private Retrofit retrofit;

    public RestClient() {
        // Custom format for dates since backend gives non-timezone'd dates which the default Gson parser needs
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();

        // Setup Retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl("http://inhollandbackend.azurewebsites.net/api/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public ArticleService createArticleService() {
        return retrofit.create(ArticleService.class);
    }

    public UserService createUserService() {
        return retrofit.create(UserService.class);
    }
}
