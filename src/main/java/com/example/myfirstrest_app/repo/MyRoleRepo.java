package com.example.myfirstrest_app.repo;

import com.example.myfirstrest_app.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyRoleRepo extends JpaRepository<Role, Long> {

    Role findRoleByRole(String roleName);

}
