package com.sutherland.domain;

import com.sutherland.entity.GamingEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author vinodh kumar thimmisetty
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class GamingInfo {
    private Long gameId;
    private String title, platform, editors_choice;
    private double score;
    private Set<String> genre = new HashSet<>();

    public GamingInfo(final String[] inputCSV) {
        this.title = inputCSV[0];
        this.platform = inputCSV[1];
        this.score = Double.parseDouble(inputCSV[2]);
        this.genre.addAll(Arrays.stream(inputCSV[3].split("\\,")).map(String::trim)
                .collect(Collectors.toSet()));
        this.editors_choice = inputCSV[4];
    }

    public GamingInfo(GamingEntity entity) {
        this.gameId = entity.getGameId();
        this.title = entity.getTitle();
        this.platform = entity.getPlatform();
        this.score = entity.getScore();
        this.genre = entity.getGenre();
        this.editors_choice = entity.getEditors_choice();
    }
}
