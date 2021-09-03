package com.taxserviceapp.business.service;

import com.taxserviceapp.data.dao.UserRepository;
import com.taxserviceapp.data.entity.User;
import com.taxserviceapp.exceptions.UserAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    private final UserRepository userRepository;

    @Autowired
    public RegistrationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void registerNewUser(User user) throws UserAlreadyExistsException {

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("Email is reserved");
        }
        userRepository.save(user);
    }
}
