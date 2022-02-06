package advisor;

import java.net.http.HttpClient;
import java.util.Scanner;

public class Global {
    public static final int PORT = 8080;

    public static final String CLIENT_ID =      "d75559e3c4c248c998e0a973b09cc1eb";
    public static final String CLIENT_SECRET =  "fc01dcaaa11a42b8adca5f3f1a69500b";

    public static final String DEFAULT_ACCT_ADDRESS = "https://accounts.spotify.com";
    public static final String AUTH_SLUG = "%s/authorize?client_id=%s&redirect_uri=http://localhost:%04d/&response_type=code%n";
    public static final String TOKEN_SLUG = "%s/api/token";

    public static final String DEFAULT_API_PAGES = "5";
    public static final String DEFAULT_API_ADDRESS = "https://api.spotify.com";
    public static final String CATEGORIES_SLUG = "%s/v1/browse/categories?limit=50";
    public static final String NEW_SLUG = "%s/v1/browse/new-releases";
    public static final String PLAYLISTS_SLUG = "%s/v1/browse/categories/%s/playlists";
    public static final String FEATURED_SLUG = "%s/v1/browse/featured-playlists";

    public static final Scanner SC = new Scanner(System.in);
    public static final HttpClient CLIENT = HttpClient.newBuilder().build();
}