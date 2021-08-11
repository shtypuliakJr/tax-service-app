package com.taxserviceapp.web.controller;

import com.taxserviceapp.business.service.RegistrationService;
import com.taxserviceapp.exceptions.UserAlreadyExistsException;
import com.taxserviceapp.web.model.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {

    private final RegistrationService registrationService;

    @Autowired
    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping("/registration")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserDTO());
        return "registration";
    }

    @PostMapping("/registration")
    public String processRegister(UserDTO user, Model model) {

        try {
            registrationService.registerNewUser(user);
        } catch (UserAlreadyExistsException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("user", user);
            return "registration";
        }
        return "redirect:/login";
    }
}
