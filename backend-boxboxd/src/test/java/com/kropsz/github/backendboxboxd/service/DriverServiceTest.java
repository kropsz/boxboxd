package com.kropsz.github.backendboxboxd.service;

import static com.kropsz.github.backendboxboxd.common.DriverConstants.VALID_DRIVER;
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

import com.kropsz.github.backendboxboxd.entities.Driver;
import com.kropsz.github.backendboxboxd.repository.DriverRepository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class DriverServiceTest {

    @Mock
    private DriverRepository driverRepository;

    @InjectMocks
    private DriverService driverService;

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("Get drivers by property successfully")
    void testGetDriversByProperty() {
        var driver = VALID_DRIVER;
        Page<Driver> page = new PageImpl<>(Collections.singletonList(driver));

        when(driverRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        Page<Driver> result = driverService.getDriversByProperty("code", "HAM", PageRequest.of(0, 1));
        assertEquals(1, result.getContent().size());
        assertEquals("HAM", result.getContent().get(0).getCode());
        assertEquals("Lewis", result.getContent().get(0).getName());

    }

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("test has property driver successfully")
    void testHasProperty() {
        
        Specification<Driver> spec = DriverService.hasProperty("code", "HAM");

        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        CriteriaQuery<?> cq = mock(CriteriaQuery.class);

        Root<Driver> root = mock(Root.class);

        Path<Object> path = mock(Path.class);
        when(root.get("code")).thenReturn(path);

        Predicate predicate = mock(Predicate.class);
        when(cb.equal(path, "HAM")).thenReturn(predicate);

        Predicate result = spec.toPredicate(root, cq, cb);

        assertEquals(predicate, result);
    }

    @Test
    @DisplayName("test order by property successfully")
    void testOrderByProperty() {
        var sort = DriverService.orderByProperty("code", null);
        assertEquals("code: ASC", sort.toString());
    }

    @Test
    @DisplayName("test order by property IllegalArgumentException")
    void testOrderByPropertyIllegalArgumentException() {
        try {
            DriverService.orderByProperty("", null);
        } catch (IllegalArgumentException e) {
            assertEquals("Property must not be null or empty", e.getMessage());
        }
    }

    @Test
    @DisplayName("test get drivers by property with null property and value")
    void testGetDriversByPropertyWithNullPropertyAndValue() {
        var driver = VALID_DRIVER;
        Page<Driver> page = new PageImpl<>(Collections.singletonList(driver));

        when(driverRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<Driver> result = driverService.getDriversByProperty(null, null, PageRequest.of(0, 1));
        assertEquals(1, result.getContent().size());
        assertEquals("HAM", result.getContent().get(0).getCode());
        assertEquals("Lewis", result.getContent().get(0).getName());
    }

    @Test
    @DisplayName("test order by property with null property")
    void testOrderByPropertyWithNullProperty() {
        assertThrows(IllegalArgumentException.class, () -> {
            DriverService.orderByProperty(null, Sort.Direction.ASC);
        });
    }

    @Test
    @DisplayName("test order by property with empty property")
    void testOrderByPropertyWithEmptyProperty() {
        assertThrows(IllegalArgumentException.class, () -> {
            DriverService.orderByProperty("", Sort.Direction.ASC);
        });
    }
}
