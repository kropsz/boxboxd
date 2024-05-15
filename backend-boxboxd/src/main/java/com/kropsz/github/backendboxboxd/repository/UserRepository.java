package com.kropsz.github.backendboxboxd.repository;

import com.kropsz.github.backendboxboxd.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String admin);
}
