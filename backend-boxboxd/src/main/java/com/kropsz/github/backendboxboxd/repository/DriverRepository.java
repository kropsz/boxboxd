package com.kropsz.github.backendboxboxd.repository;

import com.kropsz.github.backendboxboxd.entities.Driver;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepository extends JpaRepository<Driver, String> {

    List<Driver> findAll(Specification<Driver> spec, Sort sort);
}
