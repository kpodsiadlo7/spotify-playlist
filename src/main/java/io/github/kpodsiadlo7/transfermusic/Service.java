package io.github.kpodsiadlo7.transfermusic;

import io.github.kpodsiadlo7.transfermusic.client.SpotifyAuth;
import io.github.kpodsiadlo7.transfermusic.client.SpotifyClient;
import io.github.kpodsiadlo7.transfermusic.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class Service {
    /** Example playlist Id
    1Rfu5DkfW6AUiJYnfMpw7m
    0QpzKNel9MzjH7YqGKwqOG
    */

    @Value("${client.id}")
    private String CLIENT_ID;
    @Value("${client.secret}")
    private String CLIENT_SECRET;
    private static int OFFSET;
    private static String TOKEN;
    private final SpotifyAuth spotifyAuth;
    private final SpotifyClient spotifyClient;

    public Service(SpotifyAuth spotifyAuth, SpotifyClient spotifyClient) {
        this.spotifyAuth = spotifyAuth;
        this.spotifyClient = spotifyClient;
    }

    Set<AuthorSong> securityProcess(String playlistId) {
        try {
            String token = getToken(CLIENT_ID, CLIENT_SECRET);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return processResponse(playlistId);
    }

    private String getToken(String clientId, String clientSecret) {
        String auth = clientId + ":" + clientSecret;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
        Map<String, String> tokenResponse = spotifyAuth.getToken(
                "Basic " + encodedAuth,
                "client_credentials",
                "application/x-www-form-urlencoded");
        TOKEN = tokenResponse.get("access_token");
        return TOKEN;
    }

    private Set<AuthorSong> processResponse(String playlistId) {
        Set<AuthorSong> listSongs = new HashSet<>();

        getPlayListAndFillSetList(playlistId, listSongs);
        return listSongs.isEmpty() ? Set.of(new AuthorSong("Brak", "Brak")) : listSongs;
    }

    private void getPlayListAndFillSetList(String playlistId, Set<AuthorSong> listSongs) {
        Tracks responseTracks;
        setFirstPage();
        do {
            responseTracks = getPlaylist(playlistId);
            fillSetListWithAuthorSong(responseTracks, listSongs);
        } while (hasNextPage(responseTracks));
    }

    private static void setFirstPage() {
        OFFSET = 0;
    }

    private Tracks getPlaylist(String playlistId) {
        int LIMIT_TRACKS = 100;
        return spotifyClient.getPlaylist(playlistId, "Bearer " + TOKEN, OFFSET, LIMIT_TRACKS);
    }


    private void fillSetListWithAuthorSong(Tracks responseTracks, Set<AuthorSong> listSongs) {
        for (var track : responseTracks.items()) {
            TrackItem trackItem = track.track();
            Album album = trackItem.album();
            fillSetList(album, listSongs);
        }
        incrementOffsetForNextPage();
    }

    private static boolean hasNextPage(Tracks responseTracks) {
        return responseTracks.next() != null;
    }

    private static void incrementOffsetForNextPage() {
        OFFSET += 100;
    }

    private static void fillSetList(Album album, Set<AuthorSong> listSongs) {
        String songName = album.name();
        StringBuilder authorName = new StringBuilder();
        for (var artist : album.artists()) {
            authorName.append(artist.name());
        }
        listSongs.add(new AuthorSong(authorName.toString(), songName));
    }
}


