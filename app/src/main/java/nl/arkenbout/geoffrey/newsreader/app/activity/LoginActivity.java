package nl.arkenbout.geoffrey.newsreader.app.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import nl.arkenbout.geoffrey.newsreader.R;
import nl.arkenbout.geoffrey.newsreader.model.User;
import nl.arkenbout.geoffrey.newsreader.rest.RestClient;
import nl.arkenbout.geoffrey.newsreader.rest.model.LoginResult;
import nl.arkenbout.geoffrey.newsreader.rest.service.UserService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, Callback<LoginResult> {

    private EditText usernameText;
    private EditText passwordText;
    private Button loginButton;
    private TextView errorText;
    private ProgressBar progressBar;

    private UserService userService;
    private boolean loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameText = findViewById(R.id.username);
        passwordText = findViewById(R.id.password);
        errorText = findViewById(R.id.errorText);

        loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(this);

        progressBar = findViewById(R.id.progressbar);

        userService = new RestClient().createUserService();
    }

    @Override
    public void onClick(View view) {
        errorText.setVisibility(View.GONE);
        String username = usernameText.getText().toString();
        String password = passwordText.getText().toString();

        // Input validation
        if (TextUtils.isEmpty(username)) {
            errorText.setText(R.string.error_username_empty);
            errorText.setVisibility(View.VISIBLE);
            return;
        } else if (TextUtils.isEmpty(password)) {
            errorText.setText(R.string.error_password_empty);
            errorText.setVisibility(View.VISIBLE);
            return;
        }
        // Disable the login button to prevent
        loginButton.setEnabled(false);

        loading = true;
        // Show the loading icon
        progressBar.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (loading) {
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
        }, 1);

        // Login
        userService.login(username, password).enqueue(this);
    }

    @Override
    public void onResponse(@NonNull Call<LoginResult> call, @NonNull Response<LoginResult> response) {
        if (response.isSuccessful() && response.body() != null) {

            LoginResult result = response.body();

            if (result != null) {
                // success notification
                Toast.makeText(this, getResources().getText(R.string.toast_logged_in), Toast.LENGTH_SHORT).show();

                // save authtoken for authorisation with user based api calls
                User.setAuthtoken(result.getAuthToken());

                setResult(RESULT_OK);
                finish();
            } else {
                // error notification
                Toast.makeText(this, getResources().getText(R.string.toast_something_went_wrong), Toast.LENGTH_SHORT).show();
                resetLoginAttempt();
            }
        } else {
            Toast.makeText(this, getResources().getText(R.string.toast_something_went_wrong), Toast.LENGTH_SHORT).show();
            resetLoginAttempt();
        }
    }

    @Override
    public void onFailure(@NonNull Call<LoginResult> call, @NonNull Throwable t) {
        Resources resources = getResources();
        String message = resources.getString(R.string.error_login_failed, t.getLocalizedMessage());

        // error notification
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

        // reset view elements where needed
        resetLoginAttempt();
    }

    private void resetLoginAttempt() {
        loading = false;
        loginButton.setEnabled(true);
        progressBar.setVisibility(View.GONE);
    }
}
