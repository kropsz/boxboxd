package com.kropsz.github.backendboxboxd.service.like;

import org.springframework.stereotype.Component;

import com.kropsz.github.backendboxboxd.entities.like.EntityType;
import com.kropsz.github.backendboxboxd.exception.NotFoundException;
import com.kropsz.github.backendboxboxd.exception.TypeNotFoundException;
import com.kropsz.github.backendboxboxd.repository.CircuitsRepository;
import com.kropsz.github.backendboxboxd.repository.DriverRepository;
import com.kropsz.github.backendboxboxd.repository.TeamRepository;
import com.kropsz.github.backendboxboxd.repository.like.LikeRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EntityVerification {

    private final DriverRepository driverRepository;
    private final TeamRepository teamRepository;
    private final CircuitsRepository circuitsRepository;

    private final LikeRepository likeRepository;

    public boolean isLikeAlreadyExists(Long userId, String entityId) {
        return likeRepository.existsByUserIdAndEntityId(userId, entityId);
    }

    public boolean isEntityExists(String entityId, EntityType entityType) {
        switch (entityType) {
            case DRIVER:
                return driverRepository.existsById(entityId);
            case TEAM:
                return teamRepository.existsById(entityId);
            case CIRCUIT:
                return circuitsRepository.existsById(entityId);
            default:
                throw new TypeNotFoundException("Tipo não encontrado: " + entityType);
        }
    }

    public void incrementLikeCounter(String entityId, EntityType entityType) {
        switch (entityType) {
            case DRIVER:
                var driver = driverRepository.findById(entityId)
                        .orElseThrow(() -> new NotFoundException("Piloto não encontrado"));
                driver.setLikes(driver.getLikes() + 1);
                driverRepository.save(driver);
                break;
            case TEAM:
                var team = teamRepository.findById(entityId)
                        .orElseThrow(() -> new NotFoundException("Time não encontrado"));
                team.setLikes(team.getLikes() + 1);
                teamRepository.save(team);
                break;
            case CIRCUIT:
                var circuit = circuitsRepository.findById(entityId)
                        .orElseThrow(() -> new NotFoundException("Pista não encontrada"));
                circuit.setLikes(circuit.getLikes() + 1);
                circuitsRepository.save(circuit);
                break;
            default:
                throw new IllegalArgumentException("Unknown entity type: " + entityType);
        }

    }
}
