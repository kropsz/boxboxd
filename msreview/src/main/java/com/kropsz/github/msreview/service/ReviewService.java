package com.kropsz.github.msreview.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kropsz.github.msreview.entities.EntityType;
import com.kropsz.github.msreview.entities.Reviews;
import com.kropsz.github.msreview.repository.ReviewsRepository;
import com.kropsz.github.msreview.web.dto.RecivePayload;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewsRepository reviewsRepository;

    @Transactional
    public void saveReview(RecivePayload payload) {
        var review = buildReview(payload);
        reviewsRepository.save(review);
    }

    public Reviews buildReview(RecivePayload payload) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                return Reviews.builder()
                .userId(payload.getUserId())
                .review(payload.getReview())
                .entityId(payload.getEntityId())
                .likes(0)
                .type(EntityType.valueOf(payload.getType()))
                .createdAt(LocalDate.parse(payload.getCreatedAt(), formatter))
                .build();
    }

    public List<Reviews> findReviewByUserId(Long userId){
        return reviewsRepository.findByUserId(userId);
    }
}
