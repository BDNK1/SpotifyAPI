package advisor;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import objects.Album;
import objects.Category;
import objects.Playlist;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static advisor.MusicApp.*;


public class MusicAppModel {

    static List<Category> category = new ArrayList<>();
    static List<Playlist> featured = new ArrayList<>();
    static List<Playlist> playlists = new ArrayList<>();
    static List<Album> news = new ArrayList<>();

    public static List<Category> getCategoryList() {
        return category;
    }

    public static List<Playlist> getFeaturedList() {
        return featured;
    }

    public static List<Playlist> getPlaylistsList() {
        return playlists;
    }

    public static List<Album> getNewsList() {
        return news;
    }


    public static boolean checkAccess() {
        if (getAccessToken() == null) {
            System.out.println("Please, provide access for application.");
        }
        return (getAccessToken() != null);
    }

    public static void printError(HttpResponse<String> response) {
        System.out.println(JsonParser.parseString(response.body()).getAsJsonObject().get("error").getAsJsonObject().get("message").getAsString());
    }

    public static void getCategories() {
        if (!category.isEmpty()) {
            category.clear();
        }
        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + getAccessToken())
                .uri(URI.create(String.format(Global.CATEGORIES_SLUG, getResource())))
                .GET()
                .build();
        try {
            HttpResponse<String> response = Global.CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JsonArray categories = JsonParser.parseString(response.body()).getAsJsonObject().get("categories").getAsJsonObject().getAsJsonArray("items");
                categories.forEach((JsonElement e) -> category.add(new Category(e.getAsJsonObject().get("name").getAsString(), e.getAsJsonObject().get("id").getAsString())));
            } else {
                printError(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getNew() {
        if (!checkAccess()) {
            return;
        }
        if (!news.isEmpty()) {
            news.clear();
        }
        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + getAccessToken())
                .uri(URI.create(String.format(Global.NEW_SLUG, getResource())))
                .GET()
                .build();
        try {
            HttpResponse<String> response = Global.CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JsonArray albums = JsonParser.parseString(response.body()).getAsJsonObject().get("albums").getAsJsonObject().getAsJsonArray("items");
                albums.forEach((JsonElement e) -> {

                    JsonObject album = e.getAsJsonObject();
                    JsonArray artists = album.getAsJsonArray("artists");

                    List<String> authors = StreamSupport.stream(artists.spliterator(), false).
                            map((JsonElement a) -> a.getAsJsonObject().get("name").getAsString()).collect(Collectors.toList());

                    news.add(new Album(album.get("name").getAsString(), authors, album.get("external_urls").getAsJsonObject().get("spotify").getAsString()));
                });
            } else {
                printError(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void getFeatured() {
        if (!checkAccess()) {
            return;
        }
        if (!featured.isEmpty()) {
            featured.clear();
        }
        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + getAccessToken())
                .uri(URI.create(String.format(Global.FEATURED_SLUG, getResource())))
                .GET()
                .build();
        try {
            HttpResponse<String> response = Global.CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JsonArray playlists = JsonParser.parseString(response.body()).getAsJsonObject().get("playlists").getAsJsonObject().getAsJsonArray("items");
                playlists.forEach((JsonElement e) -> {
                    JsonObject pl = e.getAsJsonObject();
                    featured.add(new Playlist(pl.get("name").getAsString(), pl.get("external_urls").getAsJsonObject().get("spotify").getAsString()));
                });
            } else {
                printError(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void getPlaylists(String name) {
        if (!checkAccess()) {
            return;
        }
        if (!playlists.isEmpty()) {
            playlists.clear();
        }
        Optional<Category> res = category.stream().filter((Category c) -> c.getName().equals(name)).findAny();
        if (res.isEmpty()) {
            System.out.println("Unknown category name");
        } else {
            String id = res.get().getId();
            System.out.println(String.format(Global.PLAYLISTS_SLUG, getResource(), id));
            HttpRequest request = HttpRequest.newBuilder()
                    .header("Authorization", "Bearer " + getAccessToken())
                    .uri(URI.create(String.format(Global.PLAYLISTS_SLUG, getResource(), id)))
                    .GET()
                    .build();
            try {
                HttpResponse<String> response = Global.CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
                JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
                if (response.statusCode() == 200 && json.has("playlists")) {
                    JsonArray playl = json.get("playlists").getAsJsonObject().getAsJsonArray("items");
                    playl.forEach((JsonElement e) -> {
                        JsonObject pl = e.getAsJsonObject();
                        playlists.add(new Playlist(pl.get("name").getAsString(), pl.get("external_urls").getAsJsonObject().get("spotify").getAsString()));

                    });
                } else {
                    printError(response);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
