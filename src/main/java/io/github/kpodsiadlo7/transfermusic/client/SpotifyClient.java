package io.github.kpodsiadlo7.transfermusic.client;

import io.github.kpodsiadlo7.transfermusic.dto.ArtistResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value = "spotify-client", url = "https://api.spotify.com/v1/playlists/")
public interface SpotifyClient {

    @GetMapping("{playlistId}")
    ArtistResponse getPlaylist(@PathVariable String playlistId, @RequestHeader("Authorization") String authorization);
}
