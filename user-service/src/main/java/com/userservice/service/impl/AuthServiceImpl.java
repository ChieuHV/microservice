//package com.userservice.service.impl;
//
//import com.userservice.dto.LoginDTO;
//import com.userservice.dto.RegisterDTO;
//import com.userservice.model.User;
//import com.userservice.repository.UserServiceRepository;
//import com.userservice.security.JwtService;
//import com.userservice.service.AuthService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.web.server.ResponseStatusException;
//
//@Service
//public class AuthServiceImpl implements AuthService {
//    @Autowired
//    private UserServiceRepository userServiceRepository;
//    @Autowired
//    private JwtService jwtService;
//    @Autowired
//    private BCryptPasswordEncoder bCryptPasswordEncoder;
//
//    @Override
//    public String login(LoginDTO loginDTO) {
//        User user = userServiceRepository.findByEmail(loginDTO.getEmail())
//                .orElseThrow(() -> new ResponseStatusException(
//                        HttpStatus.UNAUTHORIZED, "Invalid credentials"));
//
//        if (!bCryptPasswordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
//            throw new ResponseStatusException(
//                    HttpStatus.UNAUTHORIZED, "Invalid credentials");
//        }
//
//        return jwtService.generateToken(user);
//    }
//
//    @Override
//    public String register(RegisterDTO registerDTO) {
//        if (userServiceRepository.findByEmail(registerDTO.getEmail()).isPresent()) {
//            throw new ResponseStatusException(
//                    HttpStatus.BAD_REQUEST, "Email already in use");
//        }
//
//        User user = new User();
//        user.setEmail(registerDTO.getEmail());
//        user.setPassword(bCryptPasswordEncoder.encode(registerDTO.getPassword()));
//        user.setName(registerDTO.getName());
//
//        userServiceRepository.save(user);
//
//        return jwtService.generateToken(user);
//    }
//}
