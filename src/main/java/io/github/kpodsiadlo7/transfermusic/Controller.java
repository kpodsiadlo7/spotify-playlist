package io.github.kpodsiadlo7.transfermusic;

import io.github.kpodsiadlo7.transfermusic.dto.AuthorSong;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
public class Controller {

    private final Service service;

    public Controller(final Service service) {
        this.service = service;
    }

    @GetMapping("/playlist")
    Set<AuthorSong> getPlaylist(@RequestParam String playlistId) {
        return service.getPlaylist(playlistId);
    }
}

