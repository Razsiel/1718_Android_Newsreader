package nl.arkenbout.geoffrey.newsreader.rest.model;


public class LoginResult {
    private String AuthToken;

    public String getAuthToken() {
        return this.AuthToken;
    }

    public void setAuthToken(String AuthToken) {
        this.AuthToken = AuthToken;
    }
}
