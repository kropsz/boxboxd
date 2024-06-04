package com.kropsz.github.backendboxboxd.service.favorite;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kropsz.github.backendboxboxd.exception.ConflictException;
import com.kropsz.github.backendboxboxd.exception.NotFoundException;
import com.kropsz.github.backendboxboxd.repository.DriverRepository;
import com.kropsz.github.backendboxboxd.repository.favorite.FavoriteDriverRepository;
import com.kropsz.github.backendboxboxd.util.factory.impl.FavoriteDriverFactory;
import com.kropsz.github.backendboxboxd.util.strategy.FavoriteStrategy;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FavoriteDriverService implements FavoriteService {

    private final FavoriteDriverRepository favoriteDriverRepository;
    private final List<FavoriteStrategy> favoriteStrategies;
    private final FavoriteDriverFactory favoriteDriverFactory;
    private final DriverRepository driverRepository;

    @Override
    public void addFavorite(String driverId, Long userId) {

        if (!driverRepository.existsById(driverId)) 
            throw new NotFoundException("Piloto não encontrado para o driverId: " + driverId);

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
    @Override
    public void removeFavorite(String driverCode, Long userId) {
        
        if (!driverRepository.existsById(driverCode)) 
            throw new NotFoundException("Piloto não encontrado para o driverCode: " + driverCode);

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
