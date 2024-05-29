package com.kropsz.github.backendboxboxd.web.controller;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kropsz.github.backendboxboxd.entities.Driver;
import com.kropsz.github.backendboxboxd.service.DriverService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/boxboxd")
@RequiredArgsConstructor
public class DriverController {
    
    private final DriverService driverService;

    @GetMapping("/drivers")
    public List<Driver> getDriversByProperty(
            @RequestParam(required = false) String property,
            @RequestParam(required = false) String value,
            @RequestParam(required = false) String orderBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction) {
        return driverService.getDriversByProperty(property, value, orderBy, direction);
    }

}
