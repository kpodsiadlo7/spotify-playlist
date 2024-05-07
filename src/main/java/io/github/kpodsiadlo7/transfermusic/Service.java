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
    private final SpotifyAuth spotifyAuth;
    private final SpotifyClient spotifyClient;

    public Service(SpotifyAuth spotifyAuth, SpotifyClient spotifyClient) {
        this.spotifyAuth = spotifyAuth;
        this.spotifyClient = spotifyClient;
    }

    Set<AuthorSong> getPlaylist(String playlistId) {
        String token = getToken(CLIENT_ID, CLIENT_SECRET);
        ArtistResponse response = spotifyClient.getPlaylist(playlistId, "Bearer " + token);
        return processResponse(response);
    }

    private String getToken(String clientId, String clientSecret) {
        String auth = clientId + ":" + clientSecret;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
        Map<String, String> tokenResponse = spotifyAuth.getToken(
                "Basic " + encodedAuth,
                "client_credentials",
                "application/x-www-form-urlencoded");
        return tokenResponse.get("access_token");
    }

    private Set<AuthorSong> processResponse(ArtistResponse response) {
        Tracks tracks = response.tracks();
        return buildMapAuthorSong(tracks);
    }

    private static Set<AuthorSong> buildMapAuthorSong(Tracks tracks) {
        Set<AuthorSong> listSongs = new HashSet<>();
        for (var track : tracks.items()) {
            TrackItem trackItem = track.track();
            Album album = trackItem.album();
            String songName = album.name();
            StringBuilder authorName = new StringBuilder();
            for (var artist : album.artists()) {
                authorName.append(artist.name());
            }
            listSongs.add(new AuthorSong(authorName.toString(), songName));
        }
        return listSongs.isEmpty() ? Set.of(new AuthorSong("Brak", "Brak")) : listSongs;
    }
}


