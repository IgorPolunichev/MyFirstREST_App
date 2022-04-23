package com.example.myfirstrest_app.controller;

import com.example.myfirstrest_app.model.Role;
import com.example.myfirstrest_app.model.User;
import com.example.myfirstrest_app.service.RoleService;
import com.example.myfirstrest_app.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

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
        modelMap.addAttribute("authUserName", userService.getUserById(authUser.getId()).getUserName());
        modelMap.addAttribute("authUser", authUser);
        StringBuilder sb = new StringBuilder();
        for (Role r : userService.getUserById(authUser.getId()).getRoles()) {
            if (r.getRole().equals("ROLE_ADMIN")) {
                sb.append("ADMIN ");
            }
            if (r.getRole().equals("ROLE_USER")) {
                sb.append("USER ");
            }
        }
        modelMap.addAttribute("authUserRole", sb.toString());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("mainPageAdmin");
        return modelAndView;
    }

    @GetMapping("/allUsers")
    public Map<String, HashMap<String, String[]>> getAllUsers() {
        Map<String, HashMap<String, String[]>> t = new HashMap<>();
        List<User> test = userService.listUsers();
        for (User u : test) {
            List<String> t1 = u.getRoles().stream().map(Role::getRole).toList();
            t.put(u.getUserName(), new HashMap<>() {{
                put("userData", new String[]{u.getId().toString()
                        , u.getUserName()
                        , u.getAge().toString()
                });
                put("roles", t1.toArray(new String[0]));

            }});
        }
        return t;
    }

    @GetMapping("/getUserById/{id}")
    public HashMap<String, String[]> getUserById(@PathVariable("id") Long id) {
        User u = userService.getUserById(id);
        HashMap<String, String[]> test = new HashMap<>();
        List<String> t = u.getRoles().stream().map(Role::getRole).toList();
        test.put("userId", new String[]{u.getId().toString()});
        test.put("userName", new String[]{u.getUserName()});
        test.put("userAge", new String[]{u.getAge().toString()});
        test.put("userRoles", t.toArray(new String[0]));
        return test;
    }

    //    @GetMapping("/getUserByName/{name}")
//    public  HashMap<String, String[]> getUserByName(@PathVariable ("name") String name){
//        User u = userService.getUserByName(name);
//        HashMap<String, String[]>  test= new HashMap<>();
//        List<String> t = u.getRoles().stream().map(Role::getRole).toList();
//        test.put("userId", new String[]{u.getId().toString()});
//        test.put("userName", new String[]{u.getUserName()});
//        test.put("userAge", new String[]{u.getAge().toString()});
//        test.put("userRoles", t.toArray(new String[0]));
//        return test;
//    }
    @DeleteMapping("{id}")
    public void deleteUserById(@PathVariable("id") Long id) {
        userService.deleteUser(id);

    }

    @PostMapping()
    public void addUser(@RequestBody Map<String, Object> userJSON) {
        User user = new User();
        user.setUserName(userJSON.get("userName").toString());
        user.setAge(Long.parseLong(userJSON.get("age").toString()));
        user.setPassword(userJSON.get("password").toString());
        List<Object> roles = (List<Object>) userJSON.get("userRoles");
        if (roles.get(0).toString().equals("ROLE_ADMIN")) {
            Set<Role> role = new HashSet<>();
            role.add(roleService.getRole(2L));
            user.setRole(role);
        }
        if (roles.get(0).equals("ROLE_USER")) {
            Set<Role> role = new HashSet<>();
            role.add(roleService.getRole(1L));
            user.setRole(role);
        }
        userService.saveUser(user);
    }

    @PutMapping()
    public void updateUser(@RequestBody Map<String, Object> userJSON) {
        userService.updateUser(userJSON);
    }


}
