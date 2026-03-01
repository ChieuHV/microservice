package com.userservice.unit.service;

import com.userservice.dto.UserDTO;
import com.userservice.model.User;
import com.userservice.repository.UserServiceRepository;
import com.userservice.service.UserService;
import com.userservice.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    //  Tạo fake object của repository, không gọi db thật
    @Mock
    private UserServiceRepository userServiceRepository;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId("1");
        user.setName("chieu");
        user.setEmail("chieu@gmail.com");
        user.setKeycloakId("kc123");
    }

    @Test
    public void testFindAllShouldReturnList() {
        // given phase(thiết lập dữ liệu và hành vi)
        when(userServiceRepository.findAll()).thenReturn(List.of(user));

        // gọi hàm thật
        List<User> users = userServiceImpl.findAll();

        //then phase(xác nhận đầu ra)
        assertEquals(1, users.size());
        assertEquals("chieu", users.getFirst().getName());

        // end phase(xác nhận hành vi)
        verify(userServiceRepository, times(1)).findAll();
    }

    @Test
    public void testFindByIdShouldReturnUserDTO() {
        when(userServiceRepository.findById("1")).thenReturn(Optional.of(user));

        UserDTO userDTO = userServiceImpl.findById("1");

        assertEquals("chieu", userDTO.getName());
        assertEquals("chieu@gmail.com", userDTO.getEmail());

        verify(userServiceRepository, times(1)).findById("1");

    }

    @Test
    public void testFindByIdShouldThrowExceptionWhenUserNotFound() {
        when(userServiceRepository.findById("11")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userServiceImpl.findById("11"));

        verify(userServiceRepository, times(1)).findById("11");
    }

    @Test
    public void testEnsureUserExistsFromToken() {
        Jwt jwt = mock(Jwt.class);

        when(jwt.getSubject()).thenReturn("kc123");
        when(userServiceRepository.findByKeycloakId("kc123")).thenReturn(Optional.of(user));

        User usertk = userServiceImpl.ensureUserExistsFromToken(jwt);

        assertEquals("chieu", usertk.getName());
        assertEquals("chieu@gmail.com", usertk.getEmail());

        verify(userServiceRepository, times(1)).findByKeycloakId("kc123");
        verify(userServiceRepository, never()).save(any(User.class));
    }

    @Test
    public void testEnsureUserExistsFromTokenCreateNewUserIfNotExists() {
        Jwt jwt = mock(Jwt.class);

        when(jwt.getSubject()).thenReturn("kc124");
        when(jwt.getClaim("email")).thenReturn("chieuhv@gmail.com");
        when(jwt.getClaim("preferred_username")).thenReturn("chieuhv");
        when(userServiceRepository.findByKeycloakId("kc124")).thenReturn(Optional.empty());

        when(userServiceRepository.save(any(User.class))).thenAnswer( invocation -> {
                User user = invocation.getArgument(0, User.class);
                user.setId("1234abc");

                return user;
           }
        );

        User result = userServiceImpl.ensureUserExistsFromToken(jwt);
        assertEquals("chieuhv", result.getName());
        assertEquals("chieuhv@gmail.com", result.getEmail());

        verify(userServiceRepository, times(1)).findByKeycloakId("kc124");
    }
}
