package io.github.kpodsiadlo7.transfermusic.dto;

import java.util.List;

public record Tracks(List<Items> items, String next) {
}
