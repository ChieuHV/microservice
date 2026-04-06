package com.demosecurity.controller;

import com.demosecurity.dto.UserLogInDTO;
import com.demosecurity.dto.UserRegisterDTO;
import com.demosecurity.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/auth/")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody UserLogInDTO userLogInDTO) {
        return ResponseEntity.ok(userService.login(userLogInDTO));
    }

    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody UserRegisterDTO userRegisterDTO) {
        System.out.println("controller register");
        return ResponseEntity.ok(userService.register(userRegisterDTO));
    }
}
