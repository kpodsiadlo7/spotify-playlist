package io.github.kpodsiadlo7.transfermusic.client;

import io.github.kpodsiadlo7.transfermusic.dto.Tracks;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "spotify-client", url = "https://api.spotify.com/v1/playlists/")
public interface SpotifyClient {

    @GetMapping("{playlistId}/tracks")
    Tracks getPlaylist(@PathVariable String playlistId,
                       @RequestHeader("Authorization") String authorization,
                       @RequestParam("offset") int offset,
                       @RequestParam("limit") int limit);
}
