package nl.arkenbout.geoffrey.newsreader.model;


public class User {

    private static String authtoken;
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public static String getAuthtoken() {
        return authtoken;
    }

    public static void setAuthtoken(String token) {
        authtoken = token;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
