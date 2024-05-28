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

import com.kropsz.github.backendboxboxd.entities.favorites.FavoriteTeam;
import com.kropsz.github.backendboxboxd.exception.ConflictException;
import com.kropsz.github.backendboxboxd.exception.NotFoundException;
import com.kropsz.github.backendboxboxd.repository.TeamRepository;
import com.kropsz.github.backendboxboxd.repository.favorite.FavoriteTeamRepository;
import com.kropsz.github.backendboxboxd.util.factory.impl.FavoriteTeamFactory;
import com.kropsz.github.backendboxboxd.util.strategy.FavoriteStrategy;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class FavoriteTeamServiceTest {
    
    @InjectMocks
    private FavoriteTeamSerivce favoriteTeamService;

    @Mock
    private FavoriteTeamRepository favoriteTeamRepository;

    @Mock
    private List<FavoriteStrategy> favoriteStrategies;

    @Mock
    private FavoriteTeamFactory favoriteTeamFactory;

    @Mock
    private TeamRepository teamRepository;

    @Test
    @DisplayName("Add favorite team sucessfull")
    void addFavoriteTeam_Sucessfull() {
        String teamId = "teamId";
        Long userId = 1L;
        when(teamRepository.existsById(teamId)).thenReturn(true);
        FavoriteStrategy strategy = mock(FavoriteStrategy.class);
        when(strategy.supports("team")).thenReturn(true);
        when(strategy.execute(teamId, userId)).thenReturn(true);
        FavoriteTeam favoriteTeam = mock(FavoriteTeam.class);
        when(favoriteTeamFactory.create(teamId, userId)).thenReturn(favoriteTeam);
    
        when(favoriteStrategies.stream()).thenReturn(Stream.of(strategy));
    
        favoriteTeamService.addFavoriteTeam(teamId, userId);
    
        verify(strategy, times(1)).supports("team");
        verify(strategy, times(1)).execute(teamId, userId);
        verify(favoriteTeamFactory, times(1)).create(teamId, userId);
        verify(favoriteTeamRepository, times(1)).save(favoriteTeam);
    }

    @Test
    @DisplayName("Add favorite team failed")
    void addFavoriteTeam_Conflict() {
        String teamId = "teamId";
        Long userId = 1L;
        when(teamRepository.existsById(teamId)).thenReturn(true);
        FavoriteStrategy strategy = mock(FavoriteStrategy.class);
        when(strategy.supports("team")).thenReturn(true);
        
        doThrow(new ConflictException("Já existe um favorito para o teamName: " + teamId + " e userId: " + userId))
            .when(strategy).execute(teamId, userId);

        when(favoriteStrategies.stream()).thenReturn(Stream.of(strategy));
        
        assertThatThrownBy(() -> favoriteTeamService.addFavoriteTeam(teamId, userId))
            .isInstanceOf(ConflictException.class);

        verify(strategy, times(1)).supports("team");
        verify(strategy, times(1)).execute(teamId, userId);
        verify(favoriteTeamFactory, times(0)).create(teamId, userId);
        verify(favoriteTeamRepository, times(0)).save(null);
    }

    @Test
    @DisplayName("Add favorite team invalid strategy")
    void addFavoriteTeam_WithoutStrategy() {
        String teamId = "teamId";
        Long userId = 1L;
        when(teamRepository.existsById(teamId)).thenReturn(true);
        FavoriteStrategy strategy = mock(FavoriteStrategy.class);
        when(strategy.supports("team")).thenReturn(false);

        when(favoriteStrategies.stream()).thenReturn(Stream.of(strategy));
        
        assertThatThrownBy(() -> favoriteTeamService.addFavoriteTeam(teamId, userId))
            .isInstanceOf(IllegalArgumentException.class);

            verify(strategy, times(1)).supports("team");
    }

    @Test
    @DisplayName("Remove favorite team sucessfull")
    void removeFavoriteTeam_Sucessfull() {
        String teamId = "teamId";
        Long userId = 1L;
        when(teamRepository.existsById(teamId)).thenReturn(true);
        FavoriteStrategy strategy = mock(FavoriteStrategy.class);
        when(strategy.supports("team")).thenReturn(true);
        when(strategy.exists(teamId, userId)).thenReturn(true);

        when(favoriteStrategies.stream()).thenReturn(Stream.of(strategy));
    
        favoriteTeamService.removeFavoriteTeam(teamId, userId);
    
        verify(strategy, times(1)).supports("team");
        verify(strategy, times(1)).exists(teamId, userId);
        verify(favoriteTeamRepository, times(1)).deleteByTeamNameAndUserId(teamId, userId);
    }

    @Test
    @DisplayName("Remove favorite team failed")
    void removeFavoriteTeam_Failed() {
        String teamId = "teamId";
        Long userId = 1L;
        when(teamRepository.existsById(teamId)).thenReturn(true);
        FavoriteStrategy strategy = mock(FavoriteStrategy.class);
        when(strategy.supports("team")).thenReturn(true);

        doThrow(new NotFoundException("Não existe um favorito para o teamName: " + teamId + " e userId: " + userId))
            .when(strategy).exists(teamId, userId);

        when(favoriteStrategies.stream()).thenReturn(Stream.of(strategy));
    
        assertThatThrownBy(() -> favoriteTeamService.removeFavoriteTeam(teamId, userId))
            .isInstanceOf(NotFoundException.class);
    
        verify(strategy, times(1)).supports("team");
        verify(strategy, times(1)).exists(teamId, userId);
    }
}
