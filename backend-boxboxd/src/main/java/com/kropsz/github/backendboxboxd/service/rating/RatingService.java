package com.kropsz.github.backendboxboxd.service.rating;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kropsz.github.backendboxboxd.entities.like.EntityType;
import com.kropsz.github.backendboxboxd.entities.rating.Rate;
import com.kropsz.github.backendboxboxd.exception.BusinessViolationException;
import com.kropsz.github.backendboxboxd.exception.NotFoundException;
import com.kropsz.github.backendboxboxd.repository.rating.RatingRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;
    private final RatingUtility ratingUtility;

    @Transactional
    public void saveRating(Long userId, String entityId, Double rating, EntityType type) {
        ratingRepository.findByUserIdAndEntityIdAndType(userId, entityId, type)
                .ifPresent(r -> {
                    throw new BusinessViolationException("Avaliação já existe");
                });

        var entity = ratingUtility.createRating(userId, entityId, rating, type);
        ratingRepository.save(entity);
        ratingUtility.updateRating(entityId, rating, type);
    }

    @Transactional
    public void deleteRating(Long userId, String entityId, EntityType type) {
        var entity = ratingRepository.findByUserIdAndEntityIdAndType(userId, entityId, type)    
                .orElseThrow(() -> new NotFoundException("Avaliação não encontrada"));
        ratingRepository.deleteByUserIdAndEntityIdAndType(userId, entityId, type);
        ratingUtility.deleteRating(entityId, entity.getRating(), type);
    }

    @Transactional
    public void updateRating(Long userId, String entityId, Double newRating, EntityType type) {
        var entity = ratingRepository.findByUserIdAndEntityIdAndType(userId, entityId, type);

        if (entity.isPresent()) {
            Rate existingRating = entity.get();
            Double oldRating = existingRating.getRating();
            existingRating.setRating(newRating);
            ratingRepository.save(existingRating);

            ratingUtility.changeRating(entityId, newRating, type, oldRating);
        } else {
            throw new NotFoundException("Avaliação não encontrada");
        }
    }
}
