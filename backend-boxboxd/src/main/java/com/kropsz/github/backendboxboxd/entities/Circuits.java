package com.kropsz.github.backendboxboxd.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_circuits")
public class Circuits {
    
    @Id
    @Column(name = "code_id")
    private String codeId;
    private String name;
    private String description;
    private String country;
    private String url;
    private byte photo;
    @Column(name = "driver_best_lap")
    private String driverBestLap;
    @Column(name = "fast_lap") 
    private String fastLap;
    private int likes;
    private int reviews;
    private double rating;
    private int ratingCount;

}
