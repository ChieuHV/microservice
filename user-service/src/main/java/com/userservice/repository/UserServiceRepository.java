package com.userservice.repository;

import com.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserServiceRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);

    Optional<User> findByKeycloakId(String keycloakId);
}
