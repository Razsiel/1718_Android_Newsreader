package nl.arkenbout.geoffrey.newsreader.service.responses;


public class LoginResult {
    private String AuthToken;

    public String getAuthToken() {
        return this.AuthToken;
    }

    public void setAuthToken(String AuthToken) {
        this.AuthToken = AuthToken;
    }
}
