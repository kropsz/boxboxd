package com.kropsz.github.backendboxboxd.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kropsz.github.backendboxboxd.exception.ConflictException;
import com.kropsz.github.backendboxboxd.exception.NotFoundException;
import com.kropsz.github.backendboxboxd.repository.favorite.FavoriteTeamRepository;
import com.kropsz.github.backendboxboxd.util.factory.impl.FavoriteTeamFactory;
import com.kropsz.github.backendboxboxd.util.strategy.FavoriteStrategy;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FavoriteTeamSerivce {
    private final FavoriteTeamRepository favoriteTeamRepository;
    private final List<FavoriteStrategy> favoriteStrategies;
    private final FavoriteTeamFactory favoriteTeamFactory;

    public void addFavoriteTeam(String teamName, Long userId) {
        FavoriteStrategy strategy = favoriteStrategies.stream()
                .filter(s -> s.supports("team"))
                .findFirst()
                .orElseThrow(
                        () -> new IllegalArgumentException("Nenhuma estratégia válida encontrada para o tipo: team"));

        if (!strategy.execute(teamName, userId)) {
            throw new ConflictException("Já existe um favorito para o teamName: " + teamName + " e userId: " + userId);
        }

        favoriteTeamRepository.save(favoriteTeamFactory.create(teamName, userId));
    }

    @Transactional
    public void removeFavoriteTeam(String teamName, Long userId) {
        FavoriteStrategy strategy = favoriteStrategies.stream()
                .filter(s -> s.supports("team"))
                .findFirst()
                .orElseThrow(
                        () -> new IllegalArgumentException("Nenhuma estratégia válida encontrada para o tipo: team"));

        if (!strategy.exists(teamName, userId)) {
            throw new NotFoundException(
                    "Não existe um favorito para o teamName: " + teamName + " e userId: " + userId);
        }

        favoriteTeamRepository.deleteByTeamNameAndUserId(teamName, userId);

    }

}
