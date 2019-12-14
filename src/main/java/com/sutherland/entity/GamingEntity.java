package com.sutherland.entity;

import com.sutherland.domain.GamingInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * @author vinodh kumar thimmisetty
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
public class GamingEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long gameId;
    private String title, platform, editors_choice;
    private double score;

    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    private Set<String> genre = new HashSet<>();

    public GamingEntity(final GamingInfo gamingInfo) {
        this.title = gamingInfo.getTitle();
        this.platform = gamingInfo.getPlatform();
        this.score = gamingInfo.getScore();
        this.genre = gamingInfo.getGenre();
        this.editors_choice = gamingInfo.getEditors_choice();
    }
}
