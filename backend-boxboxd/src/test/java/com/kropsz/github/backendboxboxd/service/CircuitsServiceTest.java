package com.kropsz.github.backendboxboxd.service;

import static com.kropsz.github.backendboxboxd.common.CircuitConstants.VALID_CIRCUIT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import com.kropsz.github.backendboxboxd.entities.Circuits;
import com.kropsz.github.backendboxboxd.repository.CircuitsRepository;
import com.kropsz.github.backendboxboxd.service.search.CircuitsService;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CircuitsServiceTest {
    
    @Mock
    private CircuitsRepository circuitsRepository;

    @InjectMocks
    private CircuitsService circuitsService;

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("Get circuits by property successfully")
    void testGetCircuitsByProperty() {
        var circuit = VALID_CIRCUIT;
        Page<Circuits> page = new PageImpl<>(Collections.singletonList(circuit));

        when(circuitsRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        Page<Circuits> result = circuitsService.getCircuitsByProperty("name", "Circuit de Monaco", PageRequest.of(0, 1));
        assertEquals(1, result.getContent().size());
        assertEquals("Circuit de Monaco", result.getContent().get(0).getName());
        assertEquals("Street circuit in Monte Carlo", result.getContent().get(0).getDescription());

    }

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("test has property circuit successfully")
    void testHasProperty(){
        Specification<Circuits> spec = CircuitsService.hasProperty("name", "Circuit de Monaco");

        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        CriteriaQuery<?> cq = mock(CriteriaQuery.class);

        Root<Circuits> root = mock(Root.class);

        Path<Object> path = mock(Path.class);
        when(root.get("name")).thenReturn(path);

        Predicate predicate = mock(Predicate.class);
        when(cb.equal(path, "Circuit de Monaco")).thenReturn(predicate);

        Predicate result = spec.toPredicate(root, cq, cb);

        assertEquals(predicate, result);
    }

    @Test
    @DisplayName("test order by team property successfully")
    void testOrderByProperty(){
        var sort = CircuitsService.orderByProperty("name", null);
        assertEquals("name: ASC", sort.toString());
    }

    @Test
    @DisplayName("(CIRCUIT) test order by property IllegalArgumentException")
    void testOrderByPropertyIllegalArgumentException() {
        try {
            CircuitsService.orderByProperty("", null);
        } catch (IllegalArgumentException e) {
            assertEquals("Property must not be null or empty", e.getMessage());
        }
    }

        @Test
    @DisplayName("test get circuits by property with null property and value")
    void testGetCircuitsByPropertyWithNullPropertyAndValue() {
        var circuit = VALID_CIRCUIT;
        Page<Circuits> page = new PageImpl<>(Collections.singletonList(circuit));

        when(circuitsRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<Circuits> result = circuitsService.getCircuitsByProperty(null, null, PageRequest.of(0, 1));
        assertEquals(1, result.getContent().size());
        assertEquals("Circuit de Monaco", result.getContent().get(0).getName());
        assertEquals("Monaco", result.getContent().get(0).getCountry());
    }

    @Test
    @DisplayName("(CIRCUIT) test order by property with null property")
    void testOrderByPropertyWithNullProperty() {
        assertThrows(IllegalArgumentException.class, () -> {
            CircuitsService.orderByProperty(null, Sort.Direction.ASC);
        });
    }

    @Test
    @DisplayName("(CIRCUIT) test order by property with empty property")
    void testOrderByPropertyWithEmptyProperty() {
        assertThrows(IllegalArgumentException.class, () -> {
            CircuitsService.orderByProperty("", Sort.Direction.ASC);
        });
    }
}
