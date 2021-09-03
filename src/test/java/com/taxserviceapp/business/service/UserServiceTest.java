package com.taxserviceapp.business.service;

import com.taxserviceapp.data.dao.UserRepository;
import com.taxserviceapp.data.entity.User;
import com.taxserviceapp.web.dto.UserDTO;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @MockBean
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Test
    void name() {
        UserDTO userDTO = UserDTO.builder()
                .userId(1L)
                .firstName("User")
                .build();
        User user = User.builder()
                .firstName("User")
                .build();

        Mockito.when(userRepository.findById(userDTO.getUserId()))
                .thenReturn(Optional.ofNullable(user));
        UserDTO userInfoById = userService.getUserInfoById(1L);
        assertEquals(userDTO.getFirstName(), userInfoById.getFirstName());
    }
}