package com.example.myfirstrest_app.service;

import com.example.myfirstrest_app.model.User;

import java.util.List;
import java.util.Map;

public interface MyUserService {

    void saveAdmin ();
    User getUserById(Long id);
    List<User> listUsers();
    User saveUser(User user);
    void updateUser (Map<String,Object> userJson);
    void deleteUser(Long id);
    User getUserByName(String name);
    String decoding(String pass);
}
