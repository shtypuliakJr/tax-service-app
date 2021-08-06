package com.taxserviceapp.controller;

import com.sun.istack.NotNull;
import com.taxserviceapp.data.entity.User;
import com.taxserviceapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class DataController {

    private final UserService userService;

    @Autowired
    public DataController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/userdata/{id}", method = RequestMethod.GET)
    //@ResponseBody
    public User get(@PathVariable("id") @NotNull Long id) {

        return userService.getUserById(id).orElse(null);
    }

    @RequestMapping(method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public User getApi() {
        return userService.getUserById(1L).orElseThrow(() -> new ResponseStatusException(HttpStatus.NO_CONTENT));
    }

    @PostMapping
    public void addNewUser(@RequestBody User userDTO) {
        // check on valid
        System.out.println("here" + userDTO);
        User user = new User(
                userDTO.getFirstName(),
                userDTO.getLastName(),
                userDTO.getUsername(),
                userDTO.getPassword(),
                userDTO.getEmail(),
                userDTO.getAge(),
                userDTO.getIpn(),
                LocalDateTime.now()
        );
        userService.addUser(user);
    }


}
