package com.kropsz.github.backendboxboxd.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String country;
    private String url;
    private byte photo;
    @Column(name = "driver_best_lap")
    private String driverBestLap;
    @Column(name = "fast_lap") 
    private String fastLap;
    private int like;
    private int reviews;
    private double rating;

}
