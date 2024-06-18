package com.kropsz.github.backendboxboxd.web.dtos.review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewPayload {
    
    private Long userId;
    private String review;
    private String type;
    private String entityId;
    private String createdAt;
}
