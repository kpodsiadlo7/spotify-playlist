package io.github.kpodsiadlo7.transfermusic;

import io.github.kpodsiadlo7.transfermusic.client.SpotifyAuth;
import io.github.kpodsiadlo7.transfermusic.client.SpotifyClient;
import io.github.kpodsiadlo7.transfermusic.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class Service {
    //1Rfu5DkfW6AUiJYnfMpw7m
    @Value("${client.id}")
    private String CLIENT_ID;
    @Value("${client.secret}")
    private String CLIENT_SECRET;
    private static final int LIMIT_TRACKS = 100;
    private static int OFFSET = 0;
    private Set<AuthorSong> listSongs = new HashSet<>();
    private static String TOKEN;
    private final SpotifyAuth spotifyAuth;
    private final SpotifyClient spotifyClient;

    public Service(SpotifyAuth spotifyAuth, SpotifyClient spotifyClient) {
        this.spotifyAuth = spotifyAuth;
        this.spotifyClient = spotifyClient;
    }

    Set<AuthorSong> securityProcess(String playlistId) {
        String token = getToken(CLIENT_ID, CLIENT_SECRET);
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
        Tracks tracks;
        do {
            tracks = getPlaylist(playlistId);
            createSetListWithAuthorSong(tracks);
        } while (tracks.next() != null);
        return listSongs.isEmpty() ? Set.of(new AuthorSong("Brak", "Brak")) : listSongs;
    }

    private Tracks getPlaylist(String playlistId) {
        return spotifyClient.getPlaylist(playlistId, "Bearer " + TOKEN, OFFSET, LIMIT_TRACKS);
    }


    private void createSetListWithAuthorSong(Tracks tracks) {
        incrementOffsetForNextPage();
        for (var track : tracks.items()) {
            TrackItem trackItem = track.track();
            Album album = trackItem.album();
            createCurrentAuthorSongAndPutIntoSetList(album, listSongs);
        }
    }

    private static void incrementOffsetForNextPage() {
        OFFSET += 100;
    }

    private static void createCurrentAuthorSongAndPutIntoSetList(Album album, Set<AuthorSong> listSongs) {
        String songName = album.name();
        StringBuilder authorName = new StringBuilder();
        for (var artist : album.artists()) {
            authorName.append(artist.name());
        }
        listSongs.add(new AuthorSong(authorName.toString(), songName));
    }
}


