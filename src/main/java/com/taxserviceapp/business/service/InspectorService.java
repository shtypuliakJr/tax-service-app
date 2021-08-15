package com.taxserviceapp.business.service;

import com.taxserviceapp.data.dao.UserRepository;
import com.taxserviceapp.data.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InspectorService {

    private final UserRepository userRepository;

    @Autowired
    public InspectorService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
