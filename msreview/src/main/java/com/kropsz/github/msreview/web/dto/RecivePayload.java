package com.kropsz.github.msreview.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecivePayload {
    
    private Long userId;
    private String review;
    private String type;
    private String entityId;
    private String createdAt;
}