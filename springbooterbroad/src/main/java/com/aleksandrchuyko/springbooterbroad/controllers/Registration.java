package com.aleksandrchuyko.springbooterbroad.controllers;


import com.aleksandrchuyko.springbooterbroad.domain.User;
import com.aleksandrchuyko.springbooterbroad.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class Registration {

    private final UserService userService;

    public Registration(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String registration() {
        return "registrationView";
    }

    @PostMapping("/registration")
    public String addUser(@Valid User userForm, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "registrationView";
        }
        if (!userService.saveUser(userForm)){
            model.addAttribute("usernameError", "User dosnt exit");
            return "registrationView";
        }
        return "redirect:/login";
    }
}
