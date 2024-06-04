package com.kropsz.github.backendboxboxd.service.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.kropsz.github.backendboxboxd.entities.Circuits;
import com.kropsz.github.backendboxboxd.repository.CircuitsRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CircuitsService {
    
    private final CircuitsRepository circuitsRepository;

    public static Specification<Circuits> hasProperty(String property, String value) {
        return (circuits, cq, cb) -> cb.equal(circuits.get(property), value);
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

    public Page<Circuits> getCircuitsByProperty(String property, String value, Pageable pageable) {
        if (property == null && value == null) {
            return circuitsRepository.findAll(pageable);
        }
        Specification<Circuits> spec = hasProperty(property, value);
        return circuitsRepository.findAll(spec, pageable);
    }
    
}
