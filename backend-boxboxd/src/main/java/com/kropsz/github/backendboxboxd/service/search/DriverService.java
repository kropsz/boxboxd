package com.kropsz.github.backendboxboxd.service.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kropsz.github.backendboxboxd.entities.Driver;
import com.kropsz.github.backendboxboxd.repository.DriverRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DriverService {
    
    private final DriverRepository driverRepository;

     public static Specification<Driver> hasProperty(String property, String value) {
        return (driver, cq, cb) -> cb.equal(driver.get(property), value);
    }

    public static Sort orderByProperty(String property, Sort.Direction direction) {
        if (direction == null) {
            direction = Sort.Direction.ASC;
        }
        if (property == null || property.isEmpty()) {
            throw new IllegalArgumentException("Property must not be null or empty");
        }
        return Sort.by(direction, property);
    }

    @Transactional(readOnly = true)
    public Page<Driver> getDriversByProperty(String property, String value, Pageable pageable) {
        if (property == null && value == null) {
            return driverRepository.findAll(pageable);
        }
        Specification<Driver> spec = hasProperty(property, value);
        return driverRepository.findAll(spec, pageable);
    }
}
