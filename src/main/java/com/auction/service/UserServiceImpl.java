package com.auction.service;
import com.auction.dao.UserRepository;
import com.auction.entities.User;
import com.auction.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
@Service
public class UserServiceImpl implements UserService{
    private UserRepository userRepository;
    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }
    @Override
    public User findById(int userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with id - " + userId));
    }
    @Override
    public User save(User theUser) {
        return userRepository.save(theUser);
    }
    @Override
    public User deleteById(int userId) {
        User userToDelete = findById(userId);
        userRepository.deleteById(userId);
        return userToDelete;
    }
    @Override
    public User updateUser(int userId, User updatedUser) {
        User existingUser = findById(userId);
        updatedUser.setId(userId);
        return userRepository.save(updatedUser);
    }
}
