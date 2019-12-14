package com.sutherland.repository;

import com.sutherland.entity.AuthKeys;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author vinodh kumar thimmisetty
 */
public interface AuthRepository extends JpaRepository<AuthKeys, Long> {
}
