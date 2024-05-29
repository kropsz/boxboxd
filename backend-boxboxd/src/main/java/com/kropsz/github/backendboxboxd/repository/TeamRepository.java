package com.kropsz.github.backendboxboxd.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import com.kropsz.github.backendboxboxd.entities.Team;

public interface TeamRepository extends JpaRepository<Team, String>{

    Page<Team> findAll(Specification<Team> spec, Pageable pageable);
    
}
