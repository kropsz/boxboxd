package com.kropsz.github.backendboxboxd.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kropsz.github.backendboxboxd.exception.ConflictException;
import com.kropsz.github.backendboxboxd.exception.NotFoundException;
import com.kropsz.github.backendboxboxd.repository.favorite.FavoriteDriverRepository;
import com.kropsz.github.backendboxboxd.util.factory.impl.FavoriteDriverFactory;
import com.kropsz.github.backendboxboxd.util.strategy.FavoriteStrategy;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FavoriteDriverService {

    private final FavoriteDriverRepository favoriteDriverRepository;
    private final List<FavoriteStrategy> favoriteStrategies;
    private final FavoriteDriverFactory favoriteDriverFactory;

    public void addFavoriteDriver(String driverId, Long userId) {
        FavoriteStrategy strategy = favoriteStrategies.stream()
                .filter(s -> s.supports("driver"))
                .findFirst()
                .orElseThrow(
                        () -> new IllegalArgumentException("Nenhuma estratégia válida encontrada para o tipo: driver"));

        if (!strategy.execute(driverId, userId)) {
            throw new ConflictException("Já existe um favorito para o driverId: " + driverId + " e userId: " + userId);
        }

        favoriteDriverRepository.save(favoriteDriverFactory.create(driverId, userId));
    }

    @Transactional
    public void removeFavoriteDriver(String driverCode, Long userId) {
        FavoriteStrategy strategy = favoriteStrategies.stream()
                .filter(s -> s.supports("driver"))
                .findFirst()
                .orElseThrow(
                        () -> new IllegalArgumentException("Nenhuma estratégia válida encontrada para o tipo: driver"));

        if (!strategy.exists(driverCode, userId)) {
            throw new NotFoundException(
                    "Não existe um favorito para o driverCode: " + driverCode + " e userId: " + userId);
        }

        favoriteDriverRepository.deleteByDriverCodeAndUserId(driverCode, userId);
    }

}
