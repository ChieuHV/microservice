//package com.userservice.controller;
//
//import com.userservice.dto.LoginDTO;
//import com.userservice.dto.RegisterDTO;
//import com.userservice.security.JwtService;
//import com.userservice.service.AuthService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping(path = "/api/v1/auth/")
//public class AuthController {
//   @Autowired
//   private AuthService authService;
//
//    @PostMapping("login")
//    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO) {
//        return ResponseEntity.ok(authService.login(loginDTO));
//    }
//
//    @PostMapping("register")
//    public ResponseEntity<?> register(@RequestBody RegisterDTO registerDTO) {
//        return ResponseEntity.ok(authService.register(registerDTO));
//    }
//
//}
