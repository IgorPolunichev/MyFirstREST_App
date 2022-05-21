package com.example.myfirstrest_app.controller;

import com.example.myfirstrest_app.model.User;
import com.example.myfirstrest_app.service.RoleService;
import com.example.myfirstrest_app.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("/mainPage")
public class adminController {

    private final UserService userService;
    public final RoleService roleService;

    public adminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping()
    public ModelAndView getMainPage(ModelMap modelMap) {
        User authUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        modelMap.addAttribute("authUser", authUser);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("mainPageAdmin");
        return modelAndView;
    }

    @GetMapping("api/getAuthUser/")
    public User getAuthUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @GetMapping("api/allUsers")
    public List<UserDetails> getAllUsers() {
        List<UserDetails> test = userService.listUsers();
        return test;
    }

    @GetMapping("api/getUserById/{id}")
    public User getUserById(@PathVariable("id") Long id) {
        User u = userService.getUserById(id);
        return u;
    }

    @DeleteMapping("api/delete/{id}")
    public void deleteUserById(@PathVariable("id") Long id) {
        userService.deleteUser(id);

    }

    @PostMapping("api/users")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        userService.saveUser(user);
        return ResponseEntity.ok().body(user);
    }

    @PutMapping("")
    public void updateUser(@RequestBody User user) {
        userService.updateUser(user);
    }


}
