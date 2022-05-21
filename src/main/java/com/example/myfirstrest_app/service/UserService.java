package com.example.myfirstrest_app.service;

import com.example.myfirstrest_app.model.Role;
import com.example.myfirstrest_app.model.User;
import com.example.myfirstrest_app.repo.MyRoleRepo;
import com.example.myfirstrest_app.repo.MyUserRepo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class UserService implements UserDetailsService, MyUserService {

    private final MyUserRepo myUserRepo;
    private final MyRoleRepo myRoleRepo;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    public UserService(MyUserRepo myUserRepo, MyRoleRepo myRoleRepo, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.myUserRepo = myUserRepo;
        this.myRoleRepo = myRoleRepo;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public void saveAdmin() {
        Role user = new Role("ROLE_USER");
        user.setId(1L);
        Role admin = new Role("ROLE_ADMIN");
        admin.setId(2L);
        myRoleRepo.save(user);
        myRoleRepo.save(admin);
        User user1 = new User( "user1", bCryptPasswordEncoder.encode("pass1"), 36L);
        Set<Role> user1Rols = new HashSet<>();
        user1Rols.add(user);
        user1Rols.add(admin);
        user1.setRole(user1Rols);

        myUserRepo.save(user1);


    }

    @Override
    public User getUserById(Long id) {
        return myUserRepo.findById(id);
    }

    @Override
    public User getUserByName(String name) {
        User u = myUserRepo.findByUserName(name);
        return u;
    }

    @Override
    public List<UserDetails> listUsers() {
        return myUserRepo.listUser();
    }

    @Override
    public void saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        myUserRepo.save(user);
    }

    @Override
    public void updateUser(User user) {
        if (!user.getPassword().equals(getUserById(user.getId()).getPassword())) {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        } else {
            user.setPassword(getUserById(user.getId()).getPassword());
        }
        myUserRepo.editUser(user);
    }

    @Override
    public void deleteUser(Long id) {
        myUserRepo.deleteById(id);

    }


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = myUserRepo.findByUserName(username);
        List<GrantedAuthority> grantedAuthorities = buildUserAuthority(user.getRoles());
        return buildUserForAuthentication(user);
    }

    private List<GrantedAuthority> buildUserAuthority(Set<Role> userRole) {
        Set<GrantedAuthority> setAuth = new HashSet<>();
        for (Role role : userRole) {
            setAuth.add(new SimpleGrantedAuthority(role.getRole()));
        }
        List<GrantedAuthority> res = new ArrayList<>(setAuth);
        return res;
    }

    private User buildUserForAuthentication(User user) {
        User user1 = new User(user.getUserName(), user.getPassword(), user.getAge(), user.getRoles());
        user1.setId(user.getId());
        return user1;
    }

    @Override
    public String decoding(String codingPass) {
        BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.encode(bc.encode(codingPass));
    }
}
