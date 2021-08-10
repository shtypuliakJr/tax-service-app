package com.taxserviceapp.business.service;

import com.taxserviceapp.data.dao.UserRepository;
import com.taxserviceapp.data.entity.User;
import com.taxserviceapp.web.model.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        System.out.println("Email:" + email);
        return userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No such user"));
    }
}
