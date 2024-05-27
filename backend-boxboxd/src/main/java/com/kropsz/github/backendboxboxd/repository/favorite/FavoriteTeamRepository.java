package com.kropsz.github.backendboxboxd.repository.favorite;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kropsz.github.backendboxboxd.entities.favorites.FavoriteTeam;

public interface FavoriteTeamRepository extends JpaRepository<FavoriteTeam, Long>{

    Optional<FavoriteTeam> findByTeamNameAndUserId(String name, Long userId);
    
}
