package com.example.myfirstrest_app.service;

import com.example.myfirstrest_app.model.Role;
import com.example.myfirstrest_app.repo.MyRoleRepo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

@Service
public class RoleService implements MyRoleService{

    private MyRoleRepo myRoleRepo;

    public RoleService(MyRoleRepo myRoleRepo) {
        this.myRoleRepo = myRoleRepo;
    }

    @Override
    public Role getRole(Long roleId) {
        return myRoleRepo.getById(roleId);
    }


}
