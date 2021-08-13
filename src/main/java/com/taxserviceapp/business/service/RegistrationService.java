package com.taxserviceapp.business.service;

import com.taxserviceapp.data.dao.UserRepository;
import com.taxserviceapp.data.entity.User;
import com.taxserviceapp.data.entity.UserRole;
import com.taxserviceapp.exceptions.UserAlreadyExistsException;
import com.taxserviceapp.web.model.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RegistrationService {

    private final UserRepository userRepository;

    @Autowired
    public RegistrationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void registerNewUser(UserDTO userDto) throws UserAlreadyExistsException {

        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("Email is reserved");
        }

        User user = User.builder()
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .age(userDto.getAge())
                .ipn(userDto.getIpn())
                .userRole(UserRole.USER)
                .dateOfRegistration(LocalDateTime.now())
                .enabled(true)
                .build();

        userRepository.save(user);
    }
}
