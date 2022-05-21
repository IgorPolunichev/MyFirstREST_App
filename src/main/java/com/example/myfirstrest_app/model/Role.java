package com.example.myfirstrest_app.model;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "table_roles")
public class Role implements GrantedAuthority {

    @Id
    private Long id;

    @Column(name = "role")
    private String role;


    public Role() {
    }
    public Role(Long id , String name){
        this.role = name;
        this.id = id;
    }

    public Role(String role) {
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public void setRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    @Override
    public String getAuthority() {
        return role;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (this.getClass() != o.getClass()) {
            return false;
        }
        Role role = (Role) o;
        return id == role.id
                && (role.equals(role.role));
    }

    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id).toHashCode();
    }

}