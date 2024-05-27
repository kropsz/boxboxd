package com.kropsz.github.backendboxboxd.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kropsz.github.backendboxboxd.entities.Team;

public interface TeamRepository extends JpaRepository<Team, Long>{
    
}
