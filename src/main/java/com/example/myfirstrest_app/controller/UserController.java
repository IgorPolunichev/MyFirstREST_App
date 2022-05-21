package com.example.myfirstrest_app.controller;

import com.example.myfirstrest_app.model.User;
import com.example.myfirstrest_app.service.MyUserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/userPage")
public class UserController {

    public final MyUserService myUserService;

    public UserController(MyUserService myUserService) {
        this.myUserService = myUserService;
    }

    @GetMapping()
    public ModelAndView showUserData(Model modelMap , Authentication authentication) {
        User authUser = (User) (User) authentication.getPrincipal();
        modelMap.addAttribute("userD", authUser);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("userPage");
        return modelAndView;
    }

    @GetMapping(value = "api/getUserAuth")
    public User getUserAuth(){
        User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return u;
    }
}