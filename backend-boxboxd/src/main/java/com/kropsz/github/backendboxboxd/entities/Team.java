package com.kropsz.github.backendboxboxd.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_team")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Team {
    @Id
    private String name;
    private String country;
    private String description;
    private byte logo;
    private byte banner;
    private String url;
    private int likes;
    private int wins;
    private int podiums;
    private int titles;
    @Column(name = "first_driver")
    private String firstDriver;
    @Column(name = "second_driver")
    private String secondDriver;
}
