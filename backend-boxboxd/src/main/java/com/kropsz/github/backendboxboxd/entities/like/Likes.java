package com.kropsz.github.backendboxboxd.entities.like;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_likes")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Likes {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String entityId;
    @Enumerated(EnumType.STRING)
    private EntityType type;
    
    public Likes(Long userId, String entityId, EntityType type) {
        this.userId = userId;
        this.entityId = entityId;
        this.type = type;
    }

    
}
