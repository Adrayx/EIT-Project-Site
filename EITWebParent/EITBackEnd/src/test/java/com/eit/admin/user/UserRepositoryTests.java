package com.eit.admin.user;

import com.eit.admin.user.UserRepository;
import com.eit.common.entity.Role;
import com.eit.common.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {

    @Autowired
    private UserRepository repo;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateNewUserWithOneRole() {
        Role roleAdmin = entityManager.find(Role.class, 1);
        User userMe = new User("andrei_moldovan352@gmail.com", "andrei2000", "Andrei", "Moldovan");
        userMe.addRole(roleAdmin);

        User savedUser = repo.save(userMe);

        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateUserWithMultipleRoles() {
        User newUser = new User("markis_iustin@gmail.com", "iustin2000", "Iustin", "Markis");
        Role roleAdmin = new Role(1);
        Role roleManger = new Role(2);

        newUser.addRole(roleAdmin);
        newUser.addRole(roleManger);

        User savedUser = repo.save(newUser);

        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testListAllUsers(){
        Iterable<User> listUsers = repo.findAll();
        listUsers.forEach(System.out::println);
    }

    @Test
    public void testGetUserById() {
        User userM = repo.findById(1).get();
        System.out.println(userM);
        assertThat(userM).isNotNull();
    }

    @Test
    public void testUpdateUserDetails() {
        User userM = repo.findById(1).get();
        userM.setEnabled(true);

        repo.save(userM);
    }

    @Test
    public void testUpdateUserRoles() {
        User userM = repo.findById(2).get();
        Role roleAdmin = new Role(1);
        userM.getRoles().remove(roleAdmin);

        repo.save(userM);
    }

    @Test
    public void testDeleteUser(){
        Integer userId = 2;
        repo.deleteById(2);
    }

    @Test
    public void testGetUserByEmail(){
        String email = "sergiu_sabou@gmail.com";
        User user = repo.getUserByEmail(email);

        assertThat(user).isNotNull();
    }

    @Test
    public void testCountById() {
        Integer id = 1;
        Long countById = repo.countById(id);
        assertThat(countById).isNotNull().isGreaterThan(0);
    }

    @Test
    public void testDisableUser(){
        Integer id = 1;
        repo.updateEnabledStatus(id, false);
    }

    @Test
    public void testEnableUser(){
        Integer id = 1;
        repo.updateEnabledStatus(id, true);
    }

    @Test
    public void testListFirstPage(){
        int pageNumber = 0;
        int pageSize = 4;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<User> page = repo.findAll(pageable);

        List<User> listUser = page.getContent();

        listUser.forEach(System.out::println);

        assertThat(listUser.size()).isEqualTo(pageSize);
    }

    @Test
    public void testSearchUsers() {
        String keyword = "markis";

        int pageNumber = 0;
        int pageSize = 4;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<User> page = repo.findAll(keyword, pageable);

        List<User> listUser = page.getContent();

        listUser.forEach(System.out::println);

        assertThat(listUser.size()).isGreaterThan(0);
    }
}
