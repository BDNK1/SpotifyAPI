package advisor;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import objects.Album;
import objects.Category;
import objects.Playlist;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static advisor.MusicAppModel.*;

public class MusicApp {
    static boolean finished = false;
    private static String accessToken;
    private static String access;
    private static int pages;
    private static String resource;

    public static String getAccessToken() {
        return accessToken;
    }

    public static String getAccess() {
        return access;
    }

    public static int getPages() {
        return pages;
    }

    public static String getResource() {
        return resource;
    }

    public MusicApp(Map<String, String> args) {
        access = args.getOrDefault("-access", Global.DEFAULT_ACCT_ADDRESS);
        resource = args.getOrDefault("-resource", Global.DEFAULT_API_ADDRESS);
        pages = Integer.parseInt(args.getOrDefault("-page", Global.DEFAULT_API_PAGES));
    }

    public void auth() {
        Authorization authorization = new Authorization(access);
        if (authorization.getAccessToken() != null) {
            accessToken = authorization.getAccessToken();
            System.out.println("---SUCCESS---");
        }
    }


    public void controller(String[] command, boolean refresh) throws IOException, InterruptedException {
        switch (command[0]) {
            case "auth":
                auth();
                break;
            case "new":
                if (refresh) MusicAppModel.getNew();
                MusicAppView.print(getNewsList());
                break;
            case "featured":
                if (refresh) MusicAppModel.getFeatured();
                MusicAppView.print(getFeaturedList());
                break;
            case "categories":
                if (refresh) MusicAppModel.getCategories();
                MusicAppView.print(getCategoryList());
                break;
            case "playlists":
                if (command.length > 1) {
                    StringBuilder playlistCategory = new StringBuilder();
                    for (int i = 1; i < command.length; i++) {
                        playlistCategory.append(" ").append(command[i]);
                    }
                    if (refresh) {
                        MusicAppModel.getCategories();
                        MusicAppModel.getPlaylists(playlistCategory.toString().trim());
                    }
                    MusicAppView.print(getPlaylistsList());
                } else {
                    System.out.println("Error: playlist name is required");
                }
                break;
            case "exit":
                //finished = true;
                System.out.println("---GOODBYE!---");
                //System.exit(0);
        }
    }

    public void run() {
        boolean refresh = true;
        String[] currentChoose = null;
        int page = 1;
        while (!finished)
            try {
                String[] command = Global.SC.nextLine().split(" ");
                if (command[0].equals("next")) {
                    refresh = false;
                    MusicAppView.page++;
                } else if (command[0].equals("prev")) {
                    if (MusicAppView.page == 1) {
                        System.out.println("No more pages.");
                        continue;
                    } else {
                        refresh = false;
                        MusicAppView.page--;
                    }
                } else {
                    refresh = true;
                    currentChoose = command;
                    MusicAppView.page = 1;
                }
                controller(currentChoose, refresh);

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

    }
}