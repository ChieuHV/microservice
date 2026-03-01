package com.userservice.service;

import com.userservice.dto.UserDTO;
import com.userservice.model.User;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

public interface UserService {
    void save(User user);

    List<User> findAll();

    UserDTO findById(String id);

    User ensureUserExistsFromToken(Jwt jwt);
}
