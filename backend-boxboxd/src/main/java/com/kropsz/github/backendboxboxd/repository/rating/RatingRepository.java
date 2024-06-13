package com.kropsz.github.backendboxboxd.repository.rating;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kropsz.github.backendboxboxd.entities.like.EntityType;
import com.kropsz.github.backendboxboxd.entities.rating.Rate;

public interface RatingRepository extends JpaRepository<Rate, Long>{

    Optional<Rate> findByUserIdAndEntityIdAndType(Long userId, String entityId, EntityType type);

    void deleteByUserIdAndEntityIdAndType(Long userId, String entityId, EntityType type);    
}
