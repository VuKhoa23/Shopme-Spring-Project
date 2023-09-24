package com.shopme.admin.user;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;


import static org.assertj.core.api.Assertions.assertThat;



@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class UserRepositoryTest {
    @Autowired
    UserRepository repo;
    @Autowired
    EntityManager entityManager;

    // empty test to create the table
    @Test
    public void createUser(){

    }

    @Test
    public void createOneRealUser(){
        // get the admin role and add to the user
        Role role = entityManager.find(Role.class, 1);
        User userKhoa = new User("khoa@haha.com", "12345", "Khoa",  "Vu");
        userKhoa.addRole(role);
        User savedUser = repo.save(userKhoa);
        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void createTwoRealUser(){
        // just need the ID of the role
        Role editorRole = new Role(3);
        Role assistantRole = new Role(5);
        User userKang = new User("kang@haha.com", "12345", "Kang", "John");
        userKang.addRole(editorRole);
        userKang.addRole(assistantRole);

        User savedUser = repo.save(userKang);
        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void listAllUser(){
        Iterable<User> users = repo.findAll();
        users.forEach(user ->{
            System.out.println(user);
        });
    }

    @Test
    public void getUserById(){
        User user = repo.findById(1).get();
        System.out.println(user);
        assertThat(user.getId()).isNotNull();
    }

    @Test
    public void updateUser(){
        User user = repo.findById(1).get();
        user.setEnabled(true);
        user.setEmail("new@haha.com");
        repo.save(user);
    }

    @Test
    public void updateUserRole(){
        User user = repo.findById(2).get();
        Role editorRole = new Role(3);
        user.getRoles().remove(editorRole);
        Role sale = new Role(2);
        user.addRole(sale);

        repo.save(user);
    }

    @Test
    public void deleteUser(){
        repo.deleteById(2);
        boolean isExists = repo.findById(2).isPresent();
        assertThat(isExists).isFalse();
    }
}
