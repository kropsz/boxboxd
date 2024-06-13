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
    private final LikeUtility likeUtility;

    public void addLike(Long userId, String entityId, EntityType entityType) {

        if (likeUtility.isLikeAlreadyExists(userId, entityId)) {
            throw new BusinessViolationException(
                    "Like ja existe para esse usuario e essa entidade de tipo: " + entityType);
        }

        if (!likeUtility.isEntityExists(entityId, entityType)) {
            throw new NotFoundException("Entidade não existe na base de dados: " + entityId);
        }

        Likes like = new Likes(
                userId,
                entityId,
                entityType);

        likeRepository.save(like);
        likeUtility.incrementLikeCounter(entityId, entityType);
    }

    @Transactional
    public void removeLike(Long userId, String entityId, EntityType entityType) {
        if (!likeUtility.isLikeAlreadyExists(userId, entityId)) {
            throw new NotFoundException(
                    "Like não existe para esse usuario e essa entidade de tipo: " + entityType);
        }
        likeUtility.decrementLikeCounter(entityId, entityType);
        likeRepository.deleteByUserIdAndEntityIdAndType(userId, entityId, entityType);
    }

}
