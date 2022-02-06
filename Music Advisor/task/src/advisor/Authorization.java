package advisor;

import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Authorization {

    private String code;
    private String accessToken;

    public Authorization(String address) {
        try {
            HttpServer server = HttpServer.create();
            server.bind(new InetSocketAddress(Global.PORT), 0);
            server.createContext("/",
                    (HttpExchange exchange) -> {
                        String respText = "";
                        String query = exchange.getRequestURI().getQuery();
                        Map<String, String> args = parseQuery(query);
                        if (args.containsKey("code")) {
                            code = args.get("code");
                            respText = "Got the code. Return back to your program.\n";
                            System.out.println("code received");
                        } else {
                            respText = "Authorization code not found. Try again.\n";
                        }
                        exchange.sendResponseHeaders(200, respText.length());
                        exchange.getResponseBody().write(respText.getBytes());
                        exchange.getResponseBody().close();
                    }

            );
            server.start();
            System.out.printf(Global.AUTH_SLUG, address, Global.CLIENT_ID, Global.PORT);
            System.out.println("waiting for code...");
            while (code == null) {
                Thread.sleep(100L);
            }
            server.stop(1);
            System.out.println("making http request for access_token...");

            HttpRequest request = HttpRequest.newBuilder()
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .header("Authorization", "Basic " + Base64.getEncoder().encodeToString(String.format("%s:%s", Global.CLIENT_ID, Global.CLIENT_SECRET).getBytes()))
                    .uri(URI.create(String.format(Global.TOKEN_SLUG, address)))
                    .POST(HttpRequest.BodyPublishers.ofString(String.format("grant_type=authorization_code&code=%s&redirect_uri=http://localhost:%04d/", code, Global.PORT)))
                    .build();

            HttpResponse<String> response = Global.CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            accessToken = JsonParser.parseString(response.body()).getAsJsonObject().get("access_token").getAsString();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    static Map<String, String> parseQuery(String query) {

        Map<String, String> map = new LinkedHashMap<>();
        if (query == null) {
            return map;
        }
//        Pattern p = Pattern.compile("(/\\w*\\?|&)(\\w+)=([^&]+)");
        Pattern p = Pattern.compile("(^|&)(\\w+)=([^&]+)");
        Matcher m = p.matcher(query);
        while (m.find()) {
//            System.out.println(m.group());
            map.put(m.group(2), m.group(3));
        }
        return map;
    }

    public String getCode() {
        return code;
    }

    public String getAccessToken() {
        return accessToken;
    }
}