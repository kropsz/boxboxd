package com.kropsz.github.msreview.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.kropsz.github.msreview.entities.EntityType;
import com.kropsz.github.msreview.entities.Reviews;

public interface ReviewsRepository extends MongoRepository<Reviews, String>{
    
        List<Reviews> findByUserId(Long userId);

        List<Reviews> findByEntityId(String entityId);

        List<Reviews> findByType(EntityType type);

        List<Reviews> findTop3ByUserIdOrderByCreatedAtDesc(Long userId);


}
