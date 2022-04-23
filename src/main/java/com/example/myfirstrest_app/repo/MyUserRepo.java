package com.example.myfirstrest_app.repo;

import com.example.myfirstrest_app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyUserRepo extends JpaRepository<User, Long> {

    User findByUserName(String name);
}
