package com.kropsz.github.backendboxboxd.service.rating.strategy;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.kropsz.github.backendboxboxd.exception.NotFoundException;
import com.kropsz.github.backendboxboxd.repository.CircuitsRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CircuitRatingStrategy implements RatingStrategy {

    private final CircuitsRepository circuitRepository;

    @Transactional
    @Override
    public void updateRating(String entityId, Double rating) {
        var circuit = circuitRepository.findById(entityId).orElseThrow(
                () -> new NotFoundException("Circuito não encontrado"));
        circuit.setRating((circuit.getRating() * circuit.getRatingCount() + rating) / (circuit.getRatingCount() + 1));
        circuit.setRatingCount(circuit.getRatingCount() + 1);
        circuitRepository.save(circuit);
    }

    @Transactional
    @Override
    public void removeRating(String entityId, Double rating) {
        var circuit = circuitRepository.findById(entityId).orElseThrow(
                () -> new NotFoundException("Circuito não encontrado"));
        circuit.setRating((circuit.getRating() * circuit.getRatingCount() - rating) / (circuit.getRatingCount() - 1));
        circuit.setRatingCount(circuit.getRatingCount() - 1);
        if (circuit.getRatingCount() == 0)
            circuit.setRating(0.0);
        
        circuitRepository.save(circuit);
    }

    @Transactional
    @Override
    public void changeRating(String entityId, Double oldRating, Double newRating) {
        var circuit = circuitRepository.findById(entityId).orElseThrow(
                () -> new NotFoundException("Circuito não encontrado"));
        circuit.setRating(
                (circuit.getRating() * circuit.getRatingCount() - oldRating + newRating) / circuit.getRatingCount());
        circuitRepository.save(circuit);
    }
}
