package com.example.myfirstrest_app.service;

import com.example.myfirstrest_app.model.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Map;

public interface MyUserService {

    void saveAdmin ();
    User getUserById(Long id);
    List<UserDetails> listUsers();
    void saveUser(User user);
    void updateUser (User user);
    void deleteUser(Long id);
    User getUserByName(String name);
    String decoding(String pass);
}
