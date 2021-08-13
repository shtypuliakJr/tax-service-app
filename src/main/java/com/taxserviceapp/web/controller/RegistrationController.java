package com.taxserviceapp.web.controller;

import com.taxserviceapp.business.service.RegistrationService;
import com.taxserviceapp.exceptions.UserAlreadyExistsException;
import com.taxserviceapp.web.model.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    public String processRegister(@Valid @ModelAttribute("user") UserDTO user,
                                  BindingResult result,
                                  Model model) {
        if (result.hasErrors()) {
            System.out.println("errors");
            model.addAttribute("user", user);
            return "registration";
        }

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
