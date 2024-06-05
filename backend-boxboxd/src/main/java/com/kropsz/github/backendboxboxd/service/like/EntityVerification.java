package com.kropsz.github.backendboxboxd.service.like;

import java.sql.Time;

import org.springframework.stereotype.Component;

import com.kropsz.github.backendboxboxd.entities.Circuits;
import com.kropsz.github.backendboxboxd.entities.Driver;
import com.kropsz.github.backendboxboxd.entities.Team;
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

    private Object findEntityById(String entityId, EntityType entityType) {
        switch (entityType) {
            case DRIVER:
                return driverRepository.findById(entityId)
                        .orElseThrow(() -> new NotFoundException("Piloto não encontrado"));
            case TEAM:
                return teamRepository.findById(entityId)
                        .orElseThrow(() -> new NotFoundException("Time não encontrado"));
            case CIRCUIT:
                return circuitsRepository.findById(entityId)
                        .orElseThrow(() -> new NotFoundException("Pista não encontrada"));
            default:
                throw new IllegalArgumentException("Tipo desconhecido de entidade: " + entityType);
        }
    }

    private void saveEntity(Object entity, EntityType entityType) {
        switch (entityType) {
            case DRIVER:
                driverRepository.save((Driver) entity);
                break;
            case TEAM:
                teamRepository.save((Team) entity);
                break;
            case CIRCUIT:
                circuitsRepository.save((Circuits) entity);
                break;
            default:
                throw new IllegalArgumentException("Tipo deseconhecido de entidade: " + entityType);
        }
    }

    public void incrementLikeCounter(String entityId, EntityType entityType) {
        var entity = findEntityById(entityId, entityType);
        if (entity instanceof Driver) {
            ((Driver) entity).setLikes(((Driver) entity).getLikes() + 1);
        } else if (entity instanceof Time) {
            ((Team) entity).setLikes(((Team) entity).getLikes() + 1);
        } else if (entity instanceof Circuits) {
            ((Circuits) entity).setLikes(((Circuits) entity).getLikes() + 1);
        }
        saveEntity(entity, entityType);
    }

    public void decrementLikeCounter(String entityId, EntityType entityType) {
        var entity = findEntityById(entityId, entityType);
        if (entity instanceof Driver) {
            ((Driver) entity).setLikes(((Driver) entity).getLikes() - 1);
        } else if (entity instanceof Team) {
            ((Team) entity).setLikes(((Team) entity).getLikes() - 1);
        } else if (entity instanceof Circuits) {
            ((Circuits) entity).setLikes(((Circuits) entity).getLikes() - 1);
        }
        saveEntity(entity, entityType);
    }

}
