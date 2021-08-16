package com.taxserviceapp.web.controller;

import com.taxserviceapp.business.service.InspectorService;
import com.taxserviceapp.data.entity.Report;
import com.taxserviceapp.data.entity.User;
import com.taxserviceapp.web.model.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/inspector")
public class InspectorController {

    private final InspectorService inspectorService;

    @Autowired
    public InspectorController(InspectorService inspectorService) {
        this.inspectorService = inspectorService;
    }

    @GetMapping("/inspector")
    public String getInspectorPage(Model model, Authentication authentication) {

        List<User> userList = inspectorService.getAllUsers();
        model.addAttribute("users", userList);

        return "inspector/inspector";
    }

    @GetMapping("/user-list")
    public String getInspectorPageUserList() {

        return "redirect:/inspector/user-list";
    }
}
