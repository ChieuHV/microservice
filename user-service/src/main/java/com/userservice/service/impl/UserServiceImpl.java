package com.userservice.service.impl;

import com.userservice.dto.UserDTO;
import com.userservice.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import com.userservice.repository.UserServiceRepository;
import com.userservice.service.UserService;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserServiceRepository userServiceRepository;

    @Override
    @CacheEvict(value = "allUsers", allEntries = true)
    public void save(User user) {
        userServiceRepository.save(user);
    }

    @Override
    @Cacheable("allUsers")
    public List<User> findAll() {
        return userServiceRepository.findAll();
    }

    @Override
    public UserDTO findById(String id) {
        return userServiceRepository.findById(id)
                .map(user -> new UserDTO(user.getId(), user.getName(), user.getEmail()))
                .orElseThrow(() -> new RuntimeException("Not Found"));
    }

    @Override
    public User ensureUserExistsFromToken(Jwt jwt) {
        String keycloakId = jwt.getSubject(); // sub

        return userServiceRepository.findByKeycloakId(keycloakId)
                .orElseGet(() -> {
                    User user = new User();
                    user.setKeycloakId(keycloakId);
                    user.setEmail(jwt.getClaim("email"));
                    user.setName(jwt.getClaim("preferred_username"));
                    return userServiceRepository.save(user);
                });
    }
}
