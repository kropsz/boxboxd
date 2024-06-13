package com.kropsz.github.backendboxboxd.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.kropsz.github.backendboxboxd.entities.like.EntityType;
import com.kropsz.github.backendboxboxd.entities.like.Likes;
import com.kropsz.github.backendboxboxd.exception.BusinessViolationException;
import com.kropsz.github.backendboxboxd.exception.NotFoundException;
import com.kropsz.github.backendboxboxd.repository.like.LikeRepository;
import com.kropsz.github.backendboxboxd.service.like.LikeService;
import com.kropsz.github.backendboxboxd.service.like.LikeUtility;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class LikeServiceTest {

    @InjectMocks
    private LikeService likeService;

    @Mock
    private LikeRepository likeRepository;

    @Mock
    private LikeUtility likeUtility;

    @Test
    @DisplayName("add like with valid data successful")
    void shouldAddLikeWhenValidData() {
        Long userId = 1L;
        String entityId = "entityId";
        EntityType entityType = EntityType.DRIVER;

        when(likeUtility.isLikeAlreadyExists(userId, entityId)).thenReturn(false);
        when(likeUtility.isEntityExists(entityId, entityType)).thenReturn(true);

        likeService.addLike(userId, entityId, entityType);

        verify(likeRepository, times(1)).save(any(Likes.class));
        verify(likeUtility, times(1)).incrementLikeCounter(entityId, entityType);
    }

    @Test
    @DisplayName("add like with like already exists should throw BusinessViolationException")
    void shouldThrowBusinessViolationExceptionWhenLikeAlreadyExists() {
        Long userId = 1L;
        String entityId = "entityId";
        EntityType entityType = EntityType.DRIVER;

        when(likeUtility.isLikeAlreadyExists(userId, entityId)).thenReturn(true);

        assertThatThrownBy(() -> likeService.addLike(userId, entityId, entityType))
                .isInstanceOf(BusinessViolationException.class)
                .hasMessageContaining("Like ja existe para esse usuario e essa entidade de tipo: " + entityType);
    }

    @Test
    @DisplayName("add like with entity does not exist should throw NotFoundException")
    void shouldThrowNotFoundExceptionWhenEntityDoesNotExist() {
        Long userId = 1L;
        String entityId = "entityId";
        EntityType entityType = EntityType.DRIVER;
        when(likeUtility.isLikeAlreadyExists(userId, entityId)).thenReturn(false);
        when(likeUtility.isEntityExists(entityId, entityType)).thenReturn(false);

        assertThatThrownBy(() -> likeService.addLike(userId, entityId, entityType))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Entidade não existe na base de dados: " + entityId);
    }

    @Test
    @DisplayName("remove like with valid data successful")
    void shouldRemoveLikeWhenLikeExists() {
        Long userId = 1L;
        String entityId = "entityId";
        EntityType entityType = EntityType.DRIVER; // Substitua pelo tipo de entidade correto

        when(likeUtility.isLikeAlreadyExists(userId, entityId)).thenReturn(true);

        likeService.removeLike(userId, entityId, entityType);

        verify(likeUtility, times(1)).decrementLikeCounter(entityId, entityType);
        verify(likeRepository, times(1)).deleteByUserIdAndEntityIdAndType(userId, entityId, entityType);
    }

    @Test
    @DisplayName("remove like with like does not exist should throw NotFoundException")
    void shouldThrowNotFoundExceptionWhenLikeDoesNotExist() {
        Long userId = 1L;
        String entityId = "entityId";
        EntityType entityType = EntityType.DRIVER;

        when(likeUtility.isLikeAlreadyExists(userId, entityId)).thenReturn(false);

        assertThatThrownBy(() -> likeService.removeLike(userId, entityId, entityType))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Like não existe para esse usuario e essa entidade de tipo: " + entityType);
    }

}