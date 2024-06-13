package com.kropsz.github.backendboxboxd.service.rating.strategy;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.kropsz.github.backendboxboxd.exception.NotFoundException;
import com.kropsz.github.backendboxboxd.repository.DriverRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DriverRatingStrategy implements RatingStrategy {

    private final DriverRepository driverRepository;

    @Transactional
    @Override
    public void updateRating(String entityId, Double rating) {
        var driver = driverRepository.findById(entityId).orElseThrow(
                () -> new NotFoundException("Piloto não encontrado"));
        driver.setRating((driver.getRating() * driver.getRatingCount() + rating) / (driver.getRatingCount() + 1));
        driver.setRatingCount(driver.getRatingCount() + 1);
        driverRepository.save(driver);
    }

    @Transactional
    @Override
    public void removeRating(String entityId, Double rating) {
        var driver = driverRepository.findById(entityId).orElseThrow(
                () -> new NotFoundException("Piloto não encontrado"));
        driver.setRating((driver.getRating() * driver.getRatingCount() - rating) / (driver.getRatingCount() - 1));
        driver.setRatingCount(driver.getRatingCount() - 1);
        if (driver.getRatingCount() == 0)
            driver.setRating(0.0);
        driverRepository.save(driver);
    }

    @Transactional
    @Override
    public void changeRating(String entityId, Double oldRating, Double newRating) {
        var driver = driverRepository.findById(entityId).orElseThrow(
                () -> new NotFoundException("Piloto não encontrado"));
        driver.setRating(
                (driver.getRating() * driver.getRatingCount() - oldRating + newRating) / driver.getRatingCount());
        driverRepository.save(driver);
    }

}
