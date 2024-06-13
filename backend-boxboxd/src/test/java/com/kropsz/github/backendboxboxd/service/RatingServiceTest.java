package com.kropsz.github.backendboxboxd.service;

import static com.kropsz.github.backendboxboxd.common.RateConstants.VALID_RATE;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.kropsz.github.backendboxboxd.entities.rating.Rate;
import com.kropsz.github.backendboxboxd.exception.BusinessViolationException;
import com.kropsz.github.backendboxboxd.exception.NotFoundException;
import com.kropsz.github.backendboxboxd.repository.rating.RatingRepository;
import com.kropsz.github.backendboxboxd.service.rating.RatingService;
import com.kropsz.github.backendboxboxd.service.rating.RatingUtility;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RatingServiceTest {

    @InjectMocks
    private RatingService ratingService;

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private RatingUtility ratingUtility;

    @Test
    @DisplayName("add rating with valid data successful")
    void shouldAddRatingWhenValidData() {
        Rate rate = VALID_RATE;
        when(ratingRepository.findByUserIdAndEntityIdAndType(rate.getUserId(), rate.getEntityId(), rate.getType()))
                .thenReturn(Optional.empty());
        when(ratingUtility.createRating(rate.getUserId(), rate.getEntityId(), rate.getRating(), rate.getType()))
                .thenReturn(rate);
        ratingService.saveRating(rate.getUserId(), rate.getEntityId(), rate.getRating(), rate.getType());

        verify(ratingRepository, times(1)).save(any(Rate.class));
        verify(ratingUtility, times(1)).updateRating(rate.getEntityId(), rate.getRating(), rate.getType());

    }

    @Test
    @DisplayName("add rating already exists throws BusinessViolationException")
    void shouldThrowBusinessViolationExceptionWhenRatingAlreadyExists() {
        Rate rate = VALID_RATE;
        when(ratingRepository.findByUserIdAndEntityIdAndType(rate.getUserId(), rate.getEntityId(), rate.getType()))
                .thenReturn(Optional.of(rate));

        assertThatThrownBy(
                () -> ratingService.saveRating(rate.getUserId(), rate.getEntityId(), rate.getRating(), rate.getType()))
                .isInstanceOf(BusinessViolationException.class)
                .hasMessage("Avaliação já existe");

        verify(ratingUtility, times(0)).updateRating(rate.getEntityId(), rate.getRating(), rate.getType());
    }

    @Test
    @DisplayName("delete rating with valid data successful")
    void shouldDeleteRatingWhenValidData() {
        Rate rate = VALID_RATE;
        when(ratingRepository.findByUserIdAndEntityIdAndType(rate.getUserId(), rate.getEntityId(), rate.getType()))
                .thenReturn(Optional.of(rate));

        ratingService.deleteRating(rate.getUserId(), rate.getEntityId(), rate.getType());

        verify(ratingRepository, times(1)).deleteByUserIdAndEntityIdAndType(rate.getUserId(), rate.getEntityId(),
                rate.getType());
        verify(ratingUtility, times(1)).deleteRating(rate.getEntityId(), rate.getRating(), rate.getType());
    }

    @Test
    @DisplayName("delete rating not found throws NotFoundException")
    void shouldThrowNotFoundExceptionWhenRatingNotFound() {
        Rate rate = VALID_RATE;
        when(ratingRepository.findByUserIdAndEntityIdAndType(rate.getUserId(), rate.getEntityId(), rate.getType()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(
                () -> ratingService.deleteRating(rate.getUserId(), rate.getEntityId(), rate.getType()))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Avaliação não encontrada");

        verify(ratingUtility, times(0)).deleteRating(rate.getEntityId(), rate.getRating(), rate.getType());
    }

    @Test
    @DisplayName("update rating with valid data successful")
    void shouldUpdateRatingWhenValidData() {
        Rate rate = VALID_RATE;
        when(ratingRepository.findByUserIdAndEntityIdAndType(rate.getUserId(), rate.getEntityId(), rate.getType()))
                .thenReturn(Optional.of(rate));

        ratingService.updateRating(rate.getUserId(), rate.getEntityId(), rate.getRating(), rate.getType());

        verify(ratingRepository, times(1)).save(any(Rate.class));
        verify(ratingUtility, times(1)).changeRating(rate.getEntityId(), rate.getRating(), rate.getType(), rate.getRating());
    }

    @Test
    @DisplayName("update rating not found throws NotFoundException")
    void shouldThrowNotFoundExceptionWhenUpdateRatingNotFound() {
        Rate rate = VALID_RATE;
        when(ratingRepository.findByUserIdAndEntityIdAndType(rate.getUserId(), rate.getEntityId(), rate.getType()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(
                () -> ratingService.updateRating(rate.getUserId(), rate.getEntityId(), rate.getRating(), rate.getType()))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Avaliação não encontrada");

        verify(ratingUtility, times(0)).changeRating(rate.getEntityId(), rate.getRating(), rate.getType(), rate.getRating());
    }
}
