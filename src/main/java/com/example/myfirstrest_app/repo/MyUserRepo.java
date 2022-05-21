package com.example.myfirstrest_app.repo;

import com.example.myfirstrest_app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface MyUserRepo {
    User findByUserName(String userName);

    void save(User user);

    User findById(Long id);

    List<UserDetails> listUser();

    void editUser(User user);

    void deleteById(Long id);
}
