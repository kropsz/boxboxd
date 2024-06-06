package com.kropsz.github.backendboxboxd.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import com.kropsz.github.backendboxboxd.entities.Circuits;


public interface CircuitsRepository extends JpaRepository<Circuits, String>{

    Page<Circuits> findAll(Specification<Circuits> spec, Pageable pageable);
    
}
