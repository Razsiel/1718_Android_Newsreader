package nl.arkenbout.geoffrey.newsreader.rest.service;


import nl.arkenbout.geoffrey.newsreader.rest.model.LoginResult;
import nl.arkenbout.geoffrey.newsreader.rest.model.RegisterResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UserService {

    @POST("users/register")
    @FormUrlEncoded
    Call<RegisterResponse> register(@Field("username") String username,
                                    @Field("password") String password);

    @POST("users/login")
    @FormUrlEncoded
    Call<LoginResult> login(@Field("username") String username,
                            @Field("password") String password);
}
