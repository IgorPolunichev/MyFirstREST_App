package com.example.myfirstrest_app.repo;


import com.example.myfirstrest_app.model.Role;
import com.example.myfirstrest_app.model.User;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class UserDaoImplMyUserRepo implements MyUserRepo {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public User findByUserName(String userName) {
        return (User) entityManager
                .createQuery("SELECT u FROM User u WHERE u.userName=:custName")
                .setParameter("custName", userName)
                .getSingleResult();

    }

    @Transactional
    @Override
    public void save(User user) {
        String sql = String.format("select u from User u where u.userName = '%s'", user.getUserName());
        try {
            User user1 = (User) entityManager.createQuery(sql).getSingleResult();
        }catch (BeanCreationException | NoResultException ignore){
            Set<Role> role = user.getRoles();
            Set<Role> roles = new HashSet<>();
            for (Role r: role){
                roles.add(entityManager.find(Role.class, r.getId()));
            }
            user.setRole(roles);
            entityManager.persist(user);
        }
    }

    @Override
    public User findById(Long id) {
        User user = entityManager.find(User.class, id);
        return user;

    }

    @Override
    @SuppressWarnings("unchecked")
    public List<UserDetails> listUser() {
        List<UserDetails> u = entityManager.createQuery("select u from User u", UserDetails.class).getResultList();
        return u;
    }

    @Override
    @Transactional
    public void editUser(User user) {

        entityManager.merge(user);

    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        User user = findById(id);
        entityManager.remove(user);
    }


}