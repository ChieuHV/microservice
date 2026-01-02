package com.userservice.service;

import com.userservice.dto.UserDTO;
import com.userservice.model.User;

import java.util.List;

public interface UserService {
    void save(User user);

    List<User> findAll();

    UserDTO findById(String id);
}
