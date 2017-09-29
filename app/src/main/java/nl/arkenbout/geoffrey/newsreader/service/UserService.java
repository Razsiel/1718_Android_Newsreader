package nl.arkenbout.geoffrey.newsreader.service;


import nl.arkenbout.geoffrey.newsreader.service.responses.LoginResult;
import nl.arkenbout.geoffrey.newsreader.service.responses.RegisterResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserService {

    @POST("users/register")
    Call<RegisterResponse> register(@Body String username, @Body String password);

    @POST("users/login")
    Call<LoginResult> login(@Body String username, @Body String password);
}
