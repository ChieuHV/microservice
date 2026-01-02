package com.userservice.controller;

import com.userservice.dto.UserDTO;
import com.userservice.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.userservice.service.UserService;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/users/")
public class UserServiceController {
    @Autowired
    private UserService userService;

    @PostMapping("save")
    public ResponseEntity<String> save(@RequestBody User user) {
        try {
            userService.save(user);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body("success");
    }

    @GetMapping("find-all")
    public ResponseEntity<List<User>> findAll() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.findAll());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("find-id/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable String id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.findById(id));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
