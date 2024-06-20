package com.kropsz.github.msreview.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.kropsz.github.msreview.entities.Like;

public interface LikeRepository extends MongoRepository<Like, String> {

    Optional<Like> findByReviewIdAndUserId(String reviewId, long long1);

}
