package com.demosecurity.service.impl;

import com.demosecurity.dto.UserLogInDTO;
import com.demosecurity.dto.UserRegisterDTO;
import com.demosecurity.model.User;
import com.demosecurity.repository.UserRepository;
import com.demosecurity.security.JwtUtil;
import com.demosecurity.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userServiceRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public String login(UserLogInDTO userLogInDTO) {
        System.out.println("login service");
        User user = userServiceRepository.findByUsername(userLogInDTO.getUsername())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Invalid credentials"));

//        String password = "123456";
//
//        // 🔹 Tạo hash ban đầu (giống lúc đăng ký)
//        String hash = BCrypt.hashpw(password, BCrypt.gensalt());
//
//        System.out.println("Hash lưu DB:");
        System.out.println( user.getPassword());

        // 🔹 Dùng lại "salt" từ hash DB
        String hashAgain = BCrypt.hashpw(userLogInDTO.getPassword(), user.getPassword());

        System.out.println("\nHash lại với cùng salt:");
        System.out.println(hashAgain);

        // 🔹 So sánh
        System.out.println("\nCó giống không: " + user.getPassword().equals(hashAgain));

        if (!bCryptPasswordEncoder.matches(userLogInDTO.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        return jwtUtil.generateToken(user);
    }

    @Override
    public String register(UserRegisterDTO userRegisterDTO) {
        System.out.println("service register");
        if (userServiceRepository.findByUsername(userRegisterDTO.getUsername()).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Email already in use");
        }

        User user = new User();
        user.setUsername(userRegisterDTO.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(userRegisterDTO.getPassword()));

        userServiceRepository.save(user);

        return jwtUtil.generateToken(user);
    }
}
