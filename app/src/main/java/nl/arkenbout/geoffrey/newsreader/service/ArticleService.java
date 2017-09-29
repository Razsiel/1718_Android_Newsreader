package nl.arkenbout.geoffrey.newsreader.service;

import nl.arkenbout.geoffrey.newsreader.service.responses.ArticleResult;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ArticleService {

    @GET("articles")
    Call<ArticleResult> getArticles(
            @Header("x-authtoken") String authtoken,
            @Query("count") Integer count,
            @Query("feed") Integer feed,
            @Query("feeds") String feeds,
            @Query("category") Integer category
    );

    @GET("articles/{id}")
    Call<ArticleResult> getArticle(
            @Header("x-authtoken") String authtoken,
            @Path("id") Integer articleId,
            @Query("count") Integer count,
            @Query("feed") Integer feed,
            @Query("feeds") String feeds,
            @Query("category") Integer category);

    @GET("articles/liked")
    Call<ArticleResult> getLikedArticles(
            @Header("x-authtoken") String authtoken);

    @PUT("articles/{id}/like")
    Call<Void> putLikeArticle(
            @Header("x-authtoken") String authtoken,
            @Path("id") Integer articleId,
            @Query("count") Integer count,
            @Query("feed") Integer feed,
            @Query("feeds") String feeds,
            @Query("category") Integer category);

    @DELETE("articles/{id}/like")
    Call<Void> deleteLikeArticle(
            @Header("x-authtoken") String authtoken,
            @Path("id") Integer articleId);

}
