package nl.arkenbout.geoffrey.newsreader.rest.service;


import nl.arkenbout.geoffrey.newsreader.model.User;
import nl.arkenbout.geoffrey.newsreader.rest.model.LoginResult;
import nl.arkenbout.geoffrey.newsreader.rest.model.RegisterResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserService {

    @POST("users/register")
    Call<RegisterResponse> register(@Body User user);

    @POST("users/login")
    Call<LoginResult> login(@Body User user);
}
