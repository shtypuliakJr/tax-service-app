package com.taxserviceapp.web.controller;

import com.taxserviceapp.business.service.RegistrationService;
import com.taxserviceapp.exceptions.UserAlreadyExistsException;
import com.taxserviceapp.utility.PojoConverter;
import com.taxserviceapp.web.dto.UserDTO;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Log4j
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
            model.addAttribute("user", user);
            return "registration";
        }

        try {
            registrationService.registerNewUser(PojoConverter.convertUserDtoToEntity(user));
            log.info("New user with id: " + user.getUserId());
        } catch (UserAlreadyExistsException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("user", user);
            return "registration";
        }
        return "redirect:/login";
    }
}