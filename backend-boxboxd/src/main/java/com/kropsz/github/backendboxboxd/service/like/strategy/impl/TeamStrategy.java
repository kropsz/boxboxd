package com.kropsz.github.backendboxboxd.service.like.strategy.impl;

import org.springframework.stereotype.Component;

import com.kropsz.github.backendboxboxd.entities.Team;
import com.kropsz.github.backendboxboxd.exception.NotFoundException;
import com.kropsz.github.backendboxboxd.repository.TeamRepository;
import com.kropsz.github.backendboxboxd.service.like.strategy.EntityStrategy;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TeamStrategy implements EntityStrategy {
    
    private final TeamRepository teamRepository;

    @Override
    public boolean existsById(String entityId) {
        return teamRepository.existsById(entityId);
    }

    @Override
    public Object findById(String entityId) {
        return teamRepository.findById(entityId)
                .orElseThrow(() -> new NotFoundException("Equipe não encontrada"));
    }

    @Override
    public void save(Object entity) {
        teamRepository.save((Team) entity);
    }

    @Override
    public void incrementLikeCounter(Object entity) {
        ((Team) entity).setLikes(((Team) entity).getLikes() + 1);
    }

    @Override
    public void decrementLikeCounter(Object entity) {
        ((Team) entity).setLikes(((Team) entity).getLikes() - 1);
    }

    

}
