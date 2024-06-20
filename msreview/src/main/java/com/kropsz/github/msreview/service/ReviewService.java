package com.kropsz.github.msreview.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kropsz.github.msreview.entities.EntityType;
import com.kropsz.github.msreview.entities.Like;
import com.kropsz.github.msreview.entities.Reviews;
import com.kropsz.github.msreview.exceptions.BusinessViolationException;
import com.kropsz.github.msreview.exceptions.NotFoundException;
import com.kropsz.github.msreview.jwt.JwtUtils;
import com.kropsz.github.msreview.repository.LikeRepository;
import com.kropsz.github.msreview.repository.ReviewsRepository;
import com.kropsz.github.msreview.web.dto.EditReview;
import com.kropsz.github.msreview.web.dto.RecivePayload;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewsRepository reviewsRepository;
    private final LikeRepository likeRepository;

    @Value("${jwt.private-key}")
    private String pathToPrivateKeyFile;

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

    public List<Reviews> findReviewByUserId(Long userId) {
        return reviewsRepository.findByUserId(userId);
    }

    public List<Reviews> findReviewByEntityId(String entityId) {
        return reviewsRepository.findByEntityId(entityId);
    }

    public List<Reviews> findReviewByType(String type) {
        return reviewsRepository.findByType(EntityType.valueOf(type));
    }

    public List<Reviews> searchReviewsLatestFromAUser(Long userId) {
        return reviewsRepository.findTop3ByUserIdOrderByCreatedAtDesc(userId);
    }

    public void editReview(String reviewId, String token, EditReview payload) {
        var userId = JwtUtils.extractUserIdFromToken(token, pathToPrivateKeyFile);
        reviewsRepository.findById(reviewId).ifPresentOrElse(review -> {
            if (review.getUserId() != Long.parseLong(userId)) {
                throw new BusinessViolationException("Usuário não é dono da review");
            }
            review.setReview(payload.review());
            reviewsRepository.save(review);
        }, () -> {
            throw new NotFoundException("Review não encontrada");
        });
    }

    public void likeReview(String reviewId, String token) {
        var userId = JwtUtils.extractUserIdFromToken(token, pathToPrivateKeyFile);
        incrementLikes(reviewId);

        likeRepository.findByReviewIdAndUserId(reviewId, Long.parseLong(userId)).ifPresentOrElse(like -> {
            throw new BusinessViolationException("Usuário já curtiu essa review");
        }, () -> {
            Like like = new Like();
            like.setReviewId(reviewId);
            like.setUserId(Long.parseLong(userId));
            likeRepository.save(like);
        });

    }

    public void dislikeReview(String reviewId, String token) {
        var userId = JwtUtils.extractUserIdFromToken(token, pathToPrivateKeyFile);
        decrementLikes(reviewId);
        likeRepository.findByReviewIdAndUserId(reviewId, Long.parseLong(userId))
                .ifPresentOrElse(likeRepository::delete, () -> {
                    throw new BusinessViolationException("Usuário não curtiu essa review");
                });
    }

    public void incrementLikes(String reviewId) {
        reviewsRepository.findById(reviewId).ifPresentOrElse(review -> {
            review.setLikes(review.getLikes() + 1);
            reviewsRepository.save(review);
        }, () -> {
            throw new NotFoundException("Review não encontrada");
        });
    }

    public void decrementLikes(String reviewId) {
        reviewsRepository.findById(reviewId).ifPresentOrElse(review -> {
            review.setLikes(review.getLikes() - 1);
            reviewsRepository.save(review);
        }, () -> {
            throw new NotFoundException("Review não encontrada");
        });
    }

    public void deleteReview(String reviewId, String token) {
        var userId = JwtUtils.extractUserIdFromToken(token, pathToPrivateKeyFile);
        reviewsRepository.findById(reviewId).ifPresentOrElse(review -> {
            if (review.getUserId() != Long.parseLong(userId)) {
                throw new BusinessViolationException("Usuário não é dono da review");
            }
            reviewsRepository.delete(review);
        }, () -> {
            throw new NotFoundException("Review não encontrada");
        });
    }

}
