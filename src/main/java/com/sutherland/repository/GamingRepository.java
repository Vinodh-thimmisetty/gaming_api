package com.sutherland.repository;

import com.sutherland.entity.GamingEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author vinodh kumar thimmisetty
 */
public interface GamingRepository extends PagingAndSortingRepository<GamingEntity, Long> {
}
