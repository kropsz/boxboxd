package com.kropsz.github.backendboxboxd.repository.like;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kropsz.github.backendboxboxd.entities.like.EntityType;
import com.kropsz.github.backendboxboxd.entities.like.Likes;

public interface LikeRepository extends JpaRepository<Likes, Long>{

    boolean existsByUserIdAndEntityId(Long userId, String entityId);

    void deleteByUserIdAndEntityIdAndType(Long userId, String entityId, EntityType entityType);
    
}
