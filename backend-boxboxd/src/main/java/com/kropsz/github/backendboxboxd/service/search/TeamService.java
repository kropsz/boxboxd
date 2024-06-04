package com.kropsz.github.backendboxboxd.service.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kropsz.github.backendboxboxd.entities.Team;
import com.kropsz.github.backendboxboxd.repository.TeamRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeamService{
    
    private final TeamRepository teamRepository;

    
    public static Specification<Team> hasProperty(String property, String value) {
        return (team, cq, cb) -> cb.equal(team.get(property), value);
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
    public Page<Team> getTeamsByProperty(String property, String value, Pageable pageable) {
        if (property == null && value == null) {
            return teamRepository.findAll(pageable);
        }
        Specification<Team> spec = hasProperty(property, value);
        return teamRepository.findAll(spec, pageable);
    }

}
