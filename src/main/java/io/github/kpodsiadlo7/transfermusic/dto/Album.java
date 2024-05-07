package io.github.kpodsiadlo7.transfermusic.dto;

import java.util.List;

public record Album(String name, List<Artists> artists) {
}
