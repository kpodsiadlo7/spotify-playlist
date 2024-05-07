package io.github.kpodsiadlo7.transfermusic.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(value = "auth-spotify", url = "https://accounts.spotify.com/api/token")
public interface SpotifyAuth {

    @PostMapping
    Map<String, String> getToken(
            @RequestHeader("Authorization") String authorization,
            @RequestParam("grant_type") String grantType,
            @RequestParam("content-type") String contentType
    );
}

