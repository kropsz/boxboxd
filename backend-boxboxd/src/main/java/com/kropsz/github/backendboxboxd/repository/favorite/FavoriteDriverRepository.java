package com.kropsz.github.backendboxboxd.repository.favorite;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kropsz.github.backendboxboxd.entities.favorites.FavoriteDriver;

public interface FavoriteDriverRepository extends JpaRepository<FavoriteDriver, Long>{
    
}
