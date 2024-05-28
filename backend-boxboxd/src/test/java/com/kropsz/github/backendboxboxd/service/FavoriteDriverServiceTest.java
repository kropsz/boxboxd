package com.kropsz.github.backendboxboxd.service;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.kropsz.github.backendboxboxd.entities.favorites.FavoriteDriver;
import com.kropsz.github.backendboxboxd.exception.ConflictException;
import com.kropsz.github.backendboxboxd.exception.NotFoundException;
import com.kropsz.github.backendboxboxd.repository.DriverRepository;
import com.kropsz.github.backendboxboxd.repository.favorite.FavoriteDriverRepository;
import com.kropsz.github.backendboxboxd.util.factory.impl.FavoriteDriverFactory;
import com.kropsz.github.backendboxboxd.util.strategy.FavoriteStrategy;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class FavoriteDriverServiceTest {

    @InjectMocks
    private FavoriteDriverService favoriteDriverService;

    @Mock
    private FavoriteDriverRepository favoriteDriverRepository;

    @Mock
    private List<FavoriteStrategy> favoriteStrategies;

    @Mock
    private FavoriteDriverFactory favoriteDriverFactory;

    @Mock
    private DriverRepository driverRepository;

    @Test
    @DisplayName("Add favorite driver sucessfull")
    void addFavoriteDriver_Sucessfull() {
        String driverId = "driverId";
        Long userId = 1L;
        when(driverRepository.existsById(driverId)).thenReturn(true);
        FavoriteStrategy strategy = mock(FavoriteStrategy.class);
        when(strategy.supports("driver")).thenReturn(true);
        when(strategy.execute(driverId, userId)).thenReturn(true);
        FavoriteDriver favoriteDriver = mock(FavoriteDriver.class);
        when(favoriteDriverFactory.create(driverId, userId)).thenReturn(favoriteDriver);
    
        when(favoriteStrategies.stream()).thenReturn(Stream.of(strategy));
    
        favoriteDriverService.addFavoriteDriver(driverId, userId);
    
        verify(strategy, times(1)).supports("driver");
        verify(strategy, times(1)).execute(driverId, userId);
        verify(favoriteDriverFactory, times(1)).create(driverId, userId);
        verify(favoriteDriverRepository, times(1)).save(favoriteDriver);  
    }

    @Test
    @DisplayName("Add favorite driver failed")
    void addFavoriteDriver_Failed() {
        String driverId = "driverId";
        Long userId = 1L;
        when(driverRepository.existsById(driverId)).thenReturn(true);
        FavoriteStrategy strategy = mock(FavoriteStrategy.class);
        when(strategy.supports("driver")).thenReturn(true);
    
        doThrow(new ConflictException("Já existe um favorito para o driverId: " + driverId + " e userId: " + userId))
            .when(strategy).execute(driverId, userId);
    
        when(favoriteStrategies.stream()).thenReturn(Stream.of(strategy));
    
        assertThatThrownBy(() -> favoriteDriverService.addFavoriteDriver(driverId, userId)).isInstanceOf(ConflictException.class);
    
        verify(strategy, times(1)).supports("driver");
        verify(strategy, times(1)).execute(driverId, userId);
    }

    @Test
    @DisplayName("Add favorite with a invalid strategy")
    void addFavoriteDriver_InvalidStrategy() {
        String driverId = "driverId";
        Long userId = 1L;
        when(driverRepository.existsById(driverId)).thenReturn(true);

        FavoriteStrategy strategy = mock(FavoriteStrategy.class);
        when(strategy.supports("driver")).thenReturn(false);
    
        when(favoriteStrategies.stream()).thenReturn(Stream.of(strategy));
    
        assertThatThrownBy(() -> favoriteDriverService.addFavoriteDriver(driverId, userId)).isInstanceOf(IllegalArgumentException.class);
    
        verify(strategy, times(1)).supports("driver");
    }

    @Test
    @DisplayName("Remove favorite driver sucessfull")
    void removeFavoriteDriver_Sucessfull() {
        String driverCode = "driverCode";
        Long userId = 1L;
        when(driverRepository.existsById(driverCode)).thenReturn(true);

        FavoriteStrategy strategy = mock(FavoriteStrategy.class);
        when(strategy.supports("driver")).thenReturn(true);
        when(strategy.exists(driverCode, userId)).thenReturn(true);
    
        when(favoriteStrategies.stream()).thenReturn(Stream.of(strategy));
    
        favoriteDriverService.removeFavoriteDriver(driverCode, userId);
    
        verify(strategy, times(1)).supports("driver");
        verify(strategy, times(1)).exists(driverCode, userId);
        verify(favoriteDriverRepository, times(1)).deleteByDriverCodeAndUserId(driverCode, userId);
    }

    @Test
    @DisplayName("Remove favorite driver failed")
    void removeFavoriteDriver_Failed() {
        String driverCode = "driverCode";
        Long userId = 1L;
        when(driverRepository.existsById(driverCode)).thenReturn(true);

        FavoriteStrategy strategy = mock(FavoriteStrategy.class);
        when(strategy.supports("driver")).thenReturn(true);

        doThrow(new NotFoundException("Não existe um favorito para o driverCode: " + driverCode + " e userId: " + userId))
            .when(strategy).exists(driverCode, userId);
        
        when(favoriteStrategies.stream()).thenReturn(Stream.of(strategy));
    
        assertThatThrownBy(() -> favoriteDriverService.removeFavoriteDriver(driverCode, userId)).isInstanceOf(NotFoundException.class);
    
        verify(strategy, times(1)).supports("driver");
        verify(strategy, times(1)).exists(driverCode, userId);
    }
}
