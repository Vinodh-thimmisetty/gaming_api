package com.sutherland.service;

import com.sutherland.domain.GamingInfo;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author vinodh kumar thimmisetty
 */
public interface GamingService {
    List<GamingInfo> findAllGames(final Pageable pageable);

    List<GamingInfo> filterGames(String title, String platform, String genre, Double score, String editor_choice, String sortBy, String sortDirection, Integer pageNumber, Integer pageSize);

    GamingInfo createGame(GamingInfo info);

    GamingInfo updateGame(Long gameId, GamingInfo info);

    GamingInfo deleteGame(Long gameId);
}
