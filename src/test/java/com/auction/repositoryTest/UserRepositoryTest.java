package com.auction.repositoryTest;

import com.auction.dao.UserRepository;
import com.auction.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Test
    @Rollback(false)
    public void testSaveUser() {
        User user = new User();
        user.setName("John Doe");
        user.setPassword("password123");
        user.setPhoneNumber("123456789");
        user.setAddress("123 Main St");
        User savedUser = userRepository.save(user);
        assertNotNull(savedUser);
        assertTrue(savedUser.getId() > 0);
        assertEquals("John Doe", savedUser.getName());
    }
    @Test
    public void testFindUserById() {
        User user = new User();
        user.setName("Jane Doe");
        user.setPassword("securepass");
        user.setPhoneNumber("987654321");
        user.setAddress("456 Another St");
        user = userRepository.save(user);
        Optional<User> foundUser = userRepository.findById(user.getId());
        assertTrue(foundUser.isPresent());
        assertEquals(user.getName(), foundUser.get().getName());
    }
    @Test
    public void testFindAllUsers() {
        User user1 = new User();
        user1.setName("User One");
        user1.setPassword("passwordOne");
        user1.setPhoneNumber("111111111");
        user1.setAddress("789 Street One");
        userRepository.save(user1);
        User user2 = new User();
        user2.setName("User Two");
        user2.setPassword("passwordTwo");
        user2.setPhoneNumber("222222222");
        user2.setAddress("101 Street Two");
        userRepository.save(user2);
        List<User> users = userRepository.findAll();
        assertFalse(users.isEmpty());
        assertTrue(users.size() >= 2);
    }
    @Test
    @Rollback(false)
    public void testUpdateUser() {
        User user = new User();
        user.setName("User to Update");
        user.setPassword("initialPassword");
        user.setPhoneNumber("333333333");
        user.setAddress("Update Street");
        user = userRepository.save(user);
        User userToUpdate = userRepository.findById(user.getId()).get();
        userToUpdate.setName("Updated User");
        userToUpdate.setPassword("updatedPassword");
        User updatedUser = userRepository.save(userToUpdate);

        assertEquals("Updated User", updatedUser.getName());
        assertEquals("updatedPassword", updatedUser.getPassword());
    }
    @Test
    @Rollback(false)
    public void testDeleteUser() {
        User user = new User();
        user.setName("User to Delete");
        user.setPassword("deletePassword");
        user.setPhoneNumber("444444444");
        user.setAddress("Delete St");
        user = userRepository.save(user);
        int userId = user.getId();
        userRepository.deleteById(userId);
        Optional<User> deletedUser = userRepository.findById(userId);
        assertFalse(deletedUser.isPresent());
    }
}
