package com.sutherland.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * @author vinodh kumar thimmisetty
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
public class AuthKeys implements Serializable {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long keyId;
    private String key;
}
