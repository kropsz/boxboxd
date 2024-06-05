package com.kropsz.github.backendboxboxd.service.like;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kropsz.github.backendboxboxd.entities.like.EntityType;
import com.kropsz.github.backendboxboxd.entities.like.Likes;
import com.kropsz.github.backendboxboxd.exception.BusinessViolationException;
import com.kropsz.github.backendboxboxd.exception.NotFoundException;
import com.kropsz.github.backendboxboxd.repository.like.LikeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final EntityVerification entityVerification;

    public void addLike(Long userId, String entityId, EntityType entityType) {

        if (entityVerification.isLikeAlreadyExists(userId, entityId)) {
            throw new BusinessViolationException(
                    "Like ja existe para esse usuario e essa entidade de tipo: " + entityType);
        }

        if (!entityVerification.isEntityExists(entityId, entityType)) {
            throw new IllegalArgumentException("Entidade não existe na base de dados: " + entityId);
        }

        Likes like = new Likes(
                userId,
                entityId,
                entityType);

        likeRepository.save(like);
    }

    @Transactional
    public void removeLike(Long userId, String entityId, EntityType entityType) {
        if (!entityVerification.isLikeAlreadyExists(userId, entityId)) {
            throw new NotFoundException(
                    "Like não existe para esse usuario e essa entidade de tipo: " + entityType);
        }
        likeRepository.deleteByUserIdAndEntityIdAndType(userId, entityId, entityType);
    }

}
