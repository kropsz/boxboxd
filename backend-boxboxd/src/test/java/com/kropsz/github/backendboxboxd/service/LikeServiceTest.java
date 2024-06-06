package com.kropsz.github.backendboxboxd.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
import com.kropsz.github.backendboxboxd.service.like.EntityVerification;
import com.kropsz.github.backendboxboxd.service.like.LikeService;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class LikeServiceTest {

    @InjectMocks
    private LikeService likeService;

    @Mock
    private LikeRepository likeRepository;

    @Mock
    private EntityVerification entityVerification;

    @Test
    @DisplayName("add like with valid data successful")
    void shouldAddLikeWhenLikeDoesNotExistAndEntityExists() {
        Long userId = 1L;
        String entityId = "entityId";
        EntityType entityType = EntityType.CIRCUIT;

        when(entityVerification.isLikeAlreadyExists(userId, entityId)).thenReturn(false);
        when(entityVerification.isEntityExists(entityId, entityType)).thenReturn(true);

        ArgumentCaptor<Likes> captor = ArgumentCaptor.forClass(Likes.class);

        likeService.addLike(userId, entityId, entityType);

        verify(likeRepository, times(1)).save(captor.capture());
        Likes savedLike = captor.getValue();

        assertEquals(userId, savedLike.getUserId());
        assertEquals(entityId, savedLike.getEntityId());
        assertEquals(entityType, savedLike.getType());

        verify(entityVerification, times(1)).incrementLikeCounter(entityId, entityType);
    }

    @Test
    void shouldThrowBusinessViolationExceptionWhenLikeAlreadyExists() {
        Long userId = 1L;
        String entityId = "entityId";
        EntityType entityType = EntityType.DRIVER;

        when(entityVerification.isLikeAlreadyExists(userId, entityId)).thenReturn(true);

        assertThrows(BusinessViolationException.class, () -> {
            likeService.addLike(userId, entityId, entityType);
        });
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenEntityDoesNotExist() {
        Long userId = 1L;
        String entityId = "entityId";
        EntityType entityType = EntityType.DRIVER;

        when(entityVerification.isLikeAlreadyExists(userId, entityId)).thenReturn(false);
        when(entityVerification.isEntityExists(entityId, entityType)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> {
            likeService.addLike(userId, entityId, entityType);
        });
    }

    @Test
void shouldRemoveLikeWhenLikeExists() {
    Long userId = 1L;
    String entityId = "entityId";
    EntityType entityType = EntityType.CIRCUIT;

    when(entityVerification.isLikeAlreadyExists(userId, entityId)).thenReturn(true);

    likeService.removeLike(userId, entityId, entityType);

    verify(entityVerification, times(1)).decrementLikeCounter(entityId, entityType);
    verify(likeRepository, times(1)).deleteByUserIdAndEntityIdAndType(userId, entityId, entityType);
}

@Test
void shouldThrowNotFoundExceptionWhenLikeDoesNotExist() {
    Long userId = 1L;
    String entityId = "entityId";
    EntityType entityType = EntityType.CIRCUIT;

    when(entityVerification.isLikeAlreadyExists(userId, entityId)).thenReturn(false);

    assertThrows(NotFoundException.class, () -> {
        likeService.removeLike(userId, entityId, entityType);
    });
}
}