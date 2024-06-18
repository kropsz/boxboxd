package com.kropsz.github.msreview.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "reviews")
@Builder
public class Reviews {
    
    @Id
    private String id;
    private Long userId;
    private String review;
    private String entityId;
    private EntityType type;
    private int likes;
    private LocalDate createdAt;
}
