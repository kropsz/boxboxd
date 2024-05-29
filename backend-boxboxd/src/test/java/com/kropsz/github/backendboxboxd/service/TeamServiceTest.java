package com.kropsz.github.backendboxboxd.service;

import static com.kropsz.github.backendboxboxd.common.TeamsConstants.VALID_TEAM;
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

import com.kropsz.github.backendboxboxd.entities.Team;
import com.kropsz.github.backendboxboxd.repository.TeamRepository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TeamServiceTest {
    
    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private TeamService teamService;

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("Get teams by property successfully")
    void testGetTeamsByProperty() {
        var team = VALID_TEAM;
        Page<Team> page = new PageImpl<>(Collections.singletonList(team));

        when(teamRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        Page<Team> result = teamService.getTeamsByProperty("name", "Ferrari", PageRequest.of(0, 1));
        assertEquals(1, result.getContent().size());
        assertEquals("Ferrari", result.getContent().get(0).getName());
        assertEquals("Italy", result.getContent().get(0).getCountry());

    }

    @Test
    @DisplayName("test hasProperty successfully")
    void testHasProperty(){
        Specification<Team> spec = TeamService.hasProperty("name", "Ferrari");
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        CriteriaQuery<?> cq = mock(CriteriaQuery.class);

        @SuppressWarnings("unchecked")
        Root<Team> root = mock(Root.class);
        @SuppressWarnings("unchecked")
        Path<Object> path = mock(Path.class);
        when(root.get("name")).thenReturn(path);
        Predicate predicate = mock(Predicate.class);
        when(cb.equal(path, "Ferrari")).thenReturn(predicate);
        Predicate result = spec.toPredicate(root, cq, cb);
        assertEquals(predicate, result);
    }

    @Test
    @DisplayName("test orderByProperty successfully")
    void testOrderByProperty(){
        var sort = TeamService.orderByProperty("name", null);
        assertEquals("name: ASC", sort.toString());
    }

    @Test
    @DisplayName("test orderByProperty IllegalArgumentException")
    void testOrderByPropertyIllegalArgumentException(){
        try {
            TeamService.orderByProperty("", null);
        } catch (IllegalArgumentException e) {
            assertEquals("Property must not be null or empty", e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("test getTeamsByProperty with null property and value")
    void testGetTeamsByPropertyWithNullPropertyAndValue(){
        var team = VALID_TEAM;
        Page<Team> page = new PageImpl<>(Collections.singletonList(team));

        when(teamRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        Page<Team> result = teamService.getTeamsByProperty("name", "Ferrari", PageRequest.of(0, 1));
        assertEquals(1, result.getContent().size());
        assertEquals("Ferrari", result.getContent().get(0).getName());
        assertEquals("Italy", result.getContent().get(0).getCountry());
    }

    @Test
    @DisplayName("test OrderByProperty with null property")
    void testOrderByPropertyWithNullProperty() {
        assertThrows(IllegalArgumentException.class, () -> {
            TeamService.orderByProperty(null, Sort.Direction.ASC);
        });
    }

    @Test
    @DisplayName("test OrderByProperty with empty property")
    void testOrderByPropertyWithEmptyProperty() {
        assertThrows(IllegalArgumentException.class, () -> {
            TeamService.orderByProperty("", Sort.Direction.ASC);
        });
    }

}
