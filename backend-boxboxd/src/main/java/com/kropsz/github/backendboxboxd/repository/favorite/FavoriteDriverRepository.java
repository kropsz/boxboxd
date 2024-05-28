package com.kropsz.github.backendboxboxd.repository.favorite;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kropsz.github.backendboxboxd.entities.favorites.FavoriteDriver;

public interface FavoriteDriverRepository extends JpaRepository<FavoriteDriver, Long>{

    Optional<FavoriteDriver> findByDriverCodeAndUserId(String code, Long userId);

    void deleteByDriverCodeAndUserId(String driverCode, Long userId);
    
}
