package com.kropsz.github.backendboxboxd.web.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kropsz.github.backendboxboxd.entities.Team;
import com.kropsz.github.backendboxboxd.service.TeamService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/boxboxd")
@RequiredArgsConstructor
public class TeamController {
    
    private final TeamService teamService;

    @GetMapping("/teams")
    public ResponseEntity<Page<Team>> getTeamsByProperty(
            @RequestParam(required = false) String property,
            @RequestParam(required = false) String value,
            @RequestParam(required = false) String orderBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
                if (orderBy == null || orderBy.isEmpty()) {
                    orderBy = "name";
                }
        Pageable pageable = PageRequest.of(page, size, direction, orderBy);
        return ResponseEntity.ok().body(teamService.getTeamsByProperty(property, value, pageable));
    }

}
