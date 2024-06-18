package com.kropsz.github.backendboxboxd.service.review;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kropsz.github.backendboxboxd.entities.like.EntityType;
import com.kropsz.github.backendboxboxd.publisher.ReviewEventPublisher;
import com.kropsz.github.backendboxboxd.web.dtos.ReviewDto;
import com.kropsz.github.backendboxboxd.web.dtos.review.ReviewPayload;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {
    
    private final ReviewEventPublisher publisher;

    public void publisherReview(Long userId, EntityType type, String entityId, ReviewDto reviewDto) throws JsonProcessingException {
        var review = createPayload(userId, type, entityId, reviewDto);
        publisher.sendReview(review);
    }



    public ReviewPayload createPayload(Long userId, EntityType type, String entityId, ReviewDto reviewDto){
        String typeAsString = String.valueOf(type);
        return ReviewPayload.builder()
            .userId(userId)
            .review(reviewDto.review())
            .type(typeAsString)
            .entityId(entityId)
            .createdAt(LocalDate.now().toString())
            .build();
    }
}
