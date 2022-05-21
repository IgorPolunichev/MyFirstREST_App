package com.example.myfirstrest_app.repo;

import com.example.myfirstrest_app.model.Role;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class RoleDaoImplMyRoleRepo implements MyRoleRepo{
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public void save(Role role) {
        if (entityManager.find(Role.class, role.getId()) == null) {
            entityManager.persist(role);
        }
    }

    @Override
    public Role getById(Long role) {
        SessionFactory sessionFactory = entityManager.getEntityManagerFactory().unwrap(SessionFactory.class);
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Role role1 = session.get(Role.class, role);
        session.close();
        return role1;
    }

    @Override
    public String[] arrayRole() {


        List<String> test = entityManager.
                createQuery("select r from Role r", Role.class)
                .getResultList().stream().map(Role::getRole).toList();
        String[] roles = test.toArray(new String[test.size()]);

        return roles;
    }

    public Role findRoleByRole(Long id) {

        return entityManager.find(Role.class , id);

    }


}