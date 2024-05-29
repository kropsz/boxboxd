package com.kropsz.github.backendboxboxd.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import com.kropsz.github.backendboxboxd.entities.Driver;

public interface DriverRepository extends JpaRepository<Driver, String> {

    Page<Driver> findAll(Specification<Driver> spec, Pageable pageable);
}
