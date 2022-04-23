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
        Role userRole = new Role("ROLE_USER");
        userRole.setId(1L);
        if (myRoleRepo.findRoleByRole(userRole.getRole()) == null) {
            myRoleRepo.save(userRole);
        }
        Role adminRole = new Role("ROLE_ADMIN");
        adminRole.setId(2L);
        if (myRoleRepo.findRoleByRole(adminRole.getRole()) == null) {
            myRoleRepo.save(adminRole);
        }

        User user = new User("user1", bCryptPasswordEncoder.encode("pass1"), 36L);
        Set<Role> userRols = new HashSet<>();
        userRols.add(myRoleRepo.getById(2L));
        userRols.add(myRoleRepo.getById(1L));
        user.setRole(userRols);

        if (myUserRepo.findByUserName(user.getUserName()) == null) {
            myUserRepo.save(user);
        }


    }

    @Override
    public User getUserById(Long id) {
        Optional<User> u  = myUserRepo.findById(id);
        return u.get();
    }
    @Override
    public User getUserByName(String name) {
       User u  = myUserRepo.findByUserName(name);
        return u;
    }

    @Override
    public List<User> listUsers() {
        return myUserRepo.findAll();
    }

    @Override
    public User saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return myUserRepo.save(user);
    }

    @Override
    public void updateUser(Map<String, Object> userJSON) {
        User user = getUserById(Long.parseLong(userJSON.get("id").toString()));
        user.setUserName(userJSON.get("userName").toString());
        user.setAge(Long.parseLong(userJSON.get("age").toString()));
        user.setPassword(userJSON.get("password")
                .toString().equals("")
                ? user.getPassword()
                : bCryptPasswordEncoder.encode(userJSON.get("password").toString()));
        List<Object> roles = (List<Object>) userJSON.get("userRoles");
        if (roles.get(0).toString().equals("ROLE_ADMIN")) {
            Set<Role> role = new HashSet<>();
            role.add(myRoleRepo.getById(2L));
            user.setRole(role);
        }
        if (roles.get(0).equals("ROLE_USER")) {
            Set<Role> role = new HashSet<>();
            role.add(myRoleRepo.getById(1L));
            user.setRole(role);
        }
        myUserRepo.save(user);

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
