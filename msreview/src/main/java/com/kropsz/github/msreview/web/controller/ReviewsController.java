package com.kropsz.github.msreview.web.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kropsz.github.msreview.service.ReviewService;
import com.kropsz.github.msreview.web.dto.RecivePayload;
import com.kropsz.github.msreview.web.dto.mapper.ReviewMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/reviews")
@RequiredArgsConstructor
public class ReviewsController {
    
    private final ReviewService reviewService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<RecivePayload>> findReviewByUserId(@PathVariable Long userId){
        return ResponseEntity.ok(ReviewMapper.toListDto(reviewService.findReviewByUserId(userId)));
    }

}
