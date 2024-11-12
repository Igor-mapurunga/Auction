package com.auction.serviceTest;

import com.auction.dao.UserRepository;
import com.auction.entities.User;
import com.auction.exception.ResourceNotFoundException;
import com.auction.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAllUsers() {
        User user1 = new User();
        user1.setId(1);
        User user2 = new User();
        user2.setId(2);

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<User> users = userService.findAll();

        assertEquals(2, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testFindUserById_UserExists() {
        User user = new User();
        user.setId(1);
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        User result = userService.findById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    public void testFindUserById_UserNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> userService.findById(1));

        assertEquals("User not found with id - 1", exception.getMessage());
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    public void testSaveUser() {
        User user = new User();
        user.setId(1);
        user.setName("John Doe");

        when(userRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userService.save(user);

        assertNotNull(savedUser);
        assertEquals("John Doe", savedUser.getName());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testUpdateUser_UserExists() {
        int userId = 1;
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setName("Old Name");

        User updatedUser = new User();
        updatedUser.setName("New Name");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        User result = userService.updateUser(userId, updatedUser);

        assertNotNull(result);
        assertEquals("New Name", result.getName());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(updatedUser);
    }

    @Test
    public void testUpdateUser_UserNotFound() {
        int userId = 1;
        User updatedUser = new User();
        updatedUser.setName("New Name");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> userService.updateUser(userId, updatedUser));

        assertEquals("User not found with id - 1", exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testDeleteUser_UserExists() {
        int userId = 1;
        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User deletedUser = userService.deleteById(userId);

        assertNotNull(deletedUser);
        assertEquals(userId, deletedUser.getId());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    public void testDeleteUser_UserNotFound() {
        int userId = 1;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> userService.deleteById(userId));

        assertEquals("User not found with id - 1", exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).deleteById(anyInt());
    }
}
