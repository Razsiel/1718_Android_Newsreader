package nl.arkenbout.geoffrey.newsreader.model;


public class User {

    private static String authtoken;

    public static String getAuthtoken() {
        return authtoken;
    }

    public static void setAuthtoken(String authtoken) {
        authtoken = authtoken;
    }
}
