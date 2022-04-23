package com.example.myfirstrest_app.controller;

import com.example.myfirstrest_app.model.Role;
import com.example.myfirstrest_app.model.User;
import com.example.myfirstrest_app.service.MyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/userPage")
public class UserController {
    @Autowired
    public MyUserService myUserService;

    @GetMapping
    public String showUserData(Model model, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        user.setPassword(myUserService.decoding(user.getPassword()));
        model.addAttribute("userD",myUserService.getUserById(user.getId()));
        model.addAttribute("authUserName", user.getUserName());
        StringBuilder sb = new StringBuilder();
        for (Role r : user.getRoles()) {
            if (r.getRole().equals("ROLE_ADMIN")) {
                sb.append("ADMIN ");
            }
            if (r.getRole().equals("ROLE_USER")) {
                sb.append("USER ");
            }
        }
        model.addAttribute("authUserRole", sb.toString());
        return "userPage";


    }
}