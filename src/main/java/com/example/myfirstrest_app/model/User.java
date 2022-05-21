package com.example.myfirstrest_app.model;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

@Entity
@Table(name = "table_user")
public class User implements UserDetails, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_Age")
    private Long age;

    @Column(name = "password", unique = true)
    private String password;

    @Column(name = "user_name", unique = true)
    private String userName;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_role"
            , joinColumns = @JoinColumn(name = "user_id")
            , inverseJoinColumns =@JoinColumn(name = "role_id"))
    private
    Set<Role> roles;

    public User(){}

    public User(String userName, String password, Long age, Set<Role> roles){
        this.password = password;
        this.userName = userName;
        this.roles = roles;
        this.age = age;
    }


    public User(String userName, String password, Long age) {
        this.password = password;
        this.userName = userName;
        this.age = age;
    }

    public Long getAge() {
        return age;
    }

    public void setAge(Long age) {
        this.age = age;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRole(Set<Role> roles) {
        this.roles = roles;
    }


    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null) return false;
        if (this.getClass() != o.getClass()) {
            return false;}
        User user = (User) o;
        return id == user.id
                && (userName.equals(user.userName));
    }

    public int hashCode(){
        return new HashCodeBuilder(17,37).append(id).toHashCode();

    }



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}