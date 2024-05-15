package com.kropsz.github.backendboxboxd.repository;

import com.kropsz.github.backendboxboxd.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
