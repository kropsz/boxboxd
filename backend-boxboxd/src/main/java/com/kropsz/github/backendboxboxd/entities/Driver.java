package com.kropsz.github.backendboxboxd.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_drivers")
public class Driver {

    @Id
    private String code;
    private String number;
    private String name;
    private String lastName;
    private byte photo;
    private String teamId;
    private String teammateId;
    private LocalDate birhDate;
    private String nationality;
    private String biography;
    private String url;
    private int firstPlaces;
    private int podiums;
    private int poles;
    private int ranking;
    private Double rating;
    private int ratingCount;
    private int likes;
    private int reviews;
}
