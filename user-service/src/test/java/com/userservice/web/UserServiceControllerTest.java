package com.userservice.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.userservice.controller.UserServiceController;
import com.userservice.dto.UserDTO;
import com.userservice.model.User;
import com.userservice.repository.UserServiceRepository;
import com.userservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserServiceController.class)
@Import(UserServiceControllerTest.TestSecurityConfig.class)
public class UserServiceControllerTest {

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UserServiceRepository userServiceRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private User mockUser;
    private UserDTO mockUserDTO;

    @BeforeEach
    public void setUp() {
        mockUser = new User();
        mockUser.setId("1234abc");
        mockUser.setName("chieu");
        mockUser.setEmail("chieu@gmail.com");

        mockUserDTO = new UserDTO();

        mockUserDTO.setId("1234abc");
        mockUserDTO.setName("chieu");
        mockUserDTO.setEmail("chieu@gmail.com");
    }

    @TestConfiguration
    static class TestSecurityConfig {
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http.csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(authorizeRequests -> authorizeRequests.anyRequest().permitAll());

            return http.build();
        }

    }

    @Test
    public void findAllShouldReturnListOfUser() throws Exception {
        when(userService.findAll()).thenReturn(List.of(mockUser));

        mockMvc.perform(get("/api/v1/users/find-all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1234abc"))
                .andExpect(jsonPath("$[0].email").value("chieu@gmail.com"));

        verify(userService, times(1)).findAll();
    }

    @Test
    public void findByIdShouldReturnListOfUser() throws Exception {
        when(userService.findById("1234abc")).thenReturn(mockUserDTO);

        mockMvc.perform(get("/api/v1/users/find-id/{id}", "1234abc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1234abc"))
                .andExpect(jsonPath("$.email").value("chieu@gmail.com"));

        verify(userService, times(1)).findById("1234abc");
    }

    @Test
    public void saveUserShouldReturnSuccess() throws Exception {
        doNothing().when(userService).save(any(User.class));

        mockMvc.perform(post("/api/v1/users/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockUserDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().string("success"));

        verify(userService, times(1)).save(any(User.class));
    }

    @Test
    void getUserByKeycloakId_shouldReturnUser_whenUserExists() throws Exception {

        when(userServiceRepository.findByKeycloakId("kc123"))
                .thenReturn(Optional.of(mockUser));

        mockMvc.perform(get("/api/v1/users/keycloak/{sub}", "kc123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("chieu"))
                .andExpect(jsonPath("$.email").value("chieu@gmail.com"));

        // ✅ verify repository được gọi
        verify(userServiceRepository, times(1))
                .findByKeycloakId("kc123");

        // ✅ verify service KHÔNG được gọi
        verify(userService, never())
                .ensureUserExistsFromToken(any());
    }

    @Test
    void getUserByKeycloakId_userNotExist_shouldCreateFromToken() throws Exception {

        when(userServiceRepository.findByKeycloakId("kc-234"))
                .thenReturn(Optional.empty());

        when(userService.ensureUserExistsFromToken(any(Jwt.class)))
                .thenReturn(mockUser);

        mockMvc.perform(
                        get("/api/v1/users/keycloak/{sub}", "kc-234")
                                .with(jwt().jwt(jwt -> jwt
                                        .subject("kc-234")
                                        .claim("email", "chieu@gmail.com")
                                        .claim("preferred_username", "chieu")
                                ))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("chieu@gmail.com"));

        verify(userService, times(1))
                .ensureUserExistsFromToken(any(Jwt.class));
    }
}
