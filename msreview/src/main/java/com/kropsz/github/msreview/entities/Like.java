package com.kropsz.github.msreview.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
@Document(collection = "likes")
public class Like {
    @Id
    private String id;
    private Long userId; 
    private String reviewId; 
}