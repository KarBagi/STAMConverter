package com.example.samtconverter.Spotify;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SpotifyAuthorization {

    private static final String CLIENT_ID = "d57e209d92954256a040dfab860c0f80";
    private static final String REDIRECT_URI = "https://example.com/callback";
    private static final String AUTH_URL = "https://accounts.spotify.com/authorize";
    private static final String TOKEN_URL = "https://accounts.spotify.com/api/token";
    private static final String PLAYLISTS_URL = "https://api.spotify.com/v1/me/playlists";
    private static final String REFRESH_TOKEN_URL = "https://accounts.spotify.com/api/token";
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final OkHttpClient CLIENT = new OkHttpClient();
    private static String accessToken = null;

    List<String> playlists = new ArrayList<>();
    List<String> playlistsId = new ArrayList<>();

    /*public static void main(String[] args) throws Exception {
        String codeVerifier = generateCodeVerifier();
        String codeChallenge = generateCodeChallenge(codeVerifier);

        String authUri = AUTH_URL + "?client_id=" + CLIENT_ID
                + "&response_type=code"
                + "&redirect_uri=" + REDIRECT_URI
                + "&code_challenge=" + codeChallenge
                + "&code_challenge_method=S256"
                + "&scope=playlist-read-private";

        refreshAccessToken(getRefreshTokenFromFile());

        System.out.println("Open the following URL in your browser:");
        System.out.println(authUri);

        System.out.println("Paste the code from the URL query parameter:");
        Scanner scanner = new Scanner(System.in);
        String authCode = scanner.nextLine();

        String accessToken = fetchAccessToken(authCode, codeVerifier);

        List<String> playlists = fetchPlaylists(accessToken);

        System.out.println(playlists.size());
        System.out.println("User's Playlists:");
        playlists.forEach(System.out::println);
    }*/

    public void initiate() throws IOException {
        refreshAccessToken(getRefreshTokenFromFile());
    }

    private static String getRefreshTokenFromFile() {
        String fileName = "C:\\Users\\Karol\\GitHub\\STAMConverter\\src\\main\\java\\com\\example\\samtconverter\\Spotify\\RefreshToken.txt";
        Path filePath = Paths.get(fileName);

        try {
            String content = new String(Files.readAllBytes(filePath));
            return content;
        } catch (IOException e) {
            System.err.println("Error reading the refresh token file: " + e.getMessage());
            return null;
        }
    }

    private static String generateCodeVerifier() {
        byte[] randomBytes = new byte[32];
        new SecureRandom().nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    private static String generateCodeChallenge(String codeVerifier) {
        try {
            byte[] bytes = java.security.MessageDigest.getInstance("SHA-256")
                    .digest(codeVerifier.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        } catch (Exception e) {
            throw new RuntimeException("Error generating code challenge", e);
        }
    }

    private static String fetchAccessToken(String authCode, String codeVerifier) throws IOException, InterruptedException {
        String body = "grant_type=authorization_code"
                + "&code=" + authCode
                + "&redirect_uri=" + REDIRECT_URI
                + "&client_id=" + CLIENT_ID
                + "&code_verifier=" + codeVerifier;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(TOKEN_URL))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JSONObject json = null;

        try {
            json = new JSONObject(response.body());

            saveRefreshTokenToFile(json.getString("refresh_token"));

            return json.getString("access_token");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void refreshAccessToken(String refreshToken) throws IOException {
        RequestBody body = new FormBody.Builder()
                .add("grant_type", "refresh_token")
                .add("refresh_token", refreshToken)
                .add("client_id", CLIENT_ID)
                .build();

        Request request = new Request.Builder()
                .url(REFRESH_TOKEN_URL)
                .post(body)
                .build();

        try (Response response = CLIENT.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code: " + response);
            }

            String responseBody = response.body().string();
            JSONObject jsonResponse = null;
            try {
                jsonResponse = new JSONObject(responseBody);
                accessToken = jsonResponse.getString("access_token");

                saveRefreshTokenToFile(jsonResponse.getString("refresh_token"));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void saveRefreshTokenToFile(String refreshToken) {
        String fileName = "C:\\Users\\Karol\\GitHub\\STAMConverter\\src\\main\\java\\com\\example\\samtconverter\\Spotify\\RefreshToken.txt";
        Path filePath = Paths.get(fileName);

        try {
            Files.write(filePath, refreshToken.getBytes());
        } catch (IOException e) {
            System.err.println("Error saving refresh token: " + e.getMessage());
        }
    }

    public List<String> fetchPlaylists() throws IOException {
        String url = "https://api.spotify.com/v1/me/playlists";
        Request request = null;

        while (!url.equals("null")) {
            try {
                request = new Request.Builder()
                        .url(url)
                        .addHeader("Authorization", "Bearer " + accessToken)
                        .build();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            try (Response response = CLIENT.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code: " + response);
                }

                String responseBody = response.body().string();
                JSONObject json = new JSONObject(responseBody);
                JSONArray items = json.getJSONArray("items");

                for (int i = 0; i < items.length(); i++) {
                    JSONObject playlist = items.optJSONObject(i);
                    if (playlist != null && playlist.has("name")) {
                        playlists.add(playlist.getString("name"));
                        playlistsId.add(playlist.getString("id"));
                    }
                }

                url = json.optString("next", "null");
            } catch (JSONException e) {
                throw new IOException("Error parsing JSON response", e);
            }
        }
        return playlists;
    }

    public List<String> fetchTracksFromPlaylist(String playlistId) throws IOException {
        List<String> tracks = new ArrayList<>();
        String url = "https://api.spotify.com/v1/playlists/" + playlistId + "/tracks";

        while (!url.equals("null")) {
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Authorization", "Bearer " + accessToken)
                    .build();

            try (Response response = CLIENT.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code: " + response);
                }

                String responseBody = response.body().string();

                JSONObject jsonResponse = new JSONObject(responseBody);

                // Sprawdzenie, czy odpowiedź zawiera pole "items"
                if (!jsonResponse.has("items")) {
                    break;
                }

                JSONArray items = jsonResponse.getJSONArray("items");

                // Zbieranie nazw utworów
                for (int i = 0; i < items.length(); i++) {
                    JSONObject trackObj = items.getJSONObject(i).getJSONObject("track");
                    String trackName = trackObj.getString("name");
                    JSONArray artists = trackObj.getJSONArray("artists");
                    String artistName = artists.getJSONObject(0).getString("name");
                    tracks.add(trackName + " - " + artistName);
                }

                // Sprawdzanie, czy jest kolejna strona z utworami
                url = jsonResponse.optString("next", "null");
            } catch (IOException e) {
                System.err.println("Error fetching playlist tracks: " + e.getMessage());
                throw e;
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        return tracks;
    }

    public List<String> getPlaylistsId() {
        return playlistsId;
    }
}
