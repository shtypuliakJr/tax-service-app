package com.taxserviceapp.business.service;

import com.taxserviceapp.data.dao.UserRepository;
import com.taxserviceapp.exceptions.NoUserFoundException;
import com.taxserviceapp.utility.PojoConverter;
import com.taxserviceapp.web.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No such user"));
    }

    public UserDTO getUserInfoById(Long id) throws NoUserFoundException {
        return userRepository.findById(id).map(PojoConverter::convertUserEntityToDto)
                .orElseThrow(() -> new NoUserFoundException("No user found by id"));
    }
}
