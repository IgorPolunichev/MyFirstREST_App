package com.example.myfirstrest_app.repo;

import com.example.myfirstrest_app.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.GrantedAuthority;

public interface MyRoleRepo {

    Role findRoleByRole(Long id);

    void save(Role role);

    Role getById(Long role);

    String[] arrayRole();

}
