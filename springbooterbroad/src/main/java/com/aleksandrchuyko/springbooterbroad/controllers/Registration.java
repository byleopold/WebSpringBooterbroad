package com.aleksandrchuyko.springbooterbroad.controllers;


import com.aleksandrchuyko.springbooterbroad.domain.User;
import com.aleksandrchuyko.springbooterbroad.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class Registration {


    @Autowired
    private UserService userService;

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
