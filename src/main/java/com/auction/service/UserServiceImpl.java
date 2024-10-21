package com.auction.service;
import com.auction.dao.UserRepository;
import com.auction.entities.User;
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
    public User findById(int theId) {
        Optional<User> result = userRepository.findById(theId);
        User theUser = null;
        if (result.isPresent()) {
            theUser = result.get();
        }else{
            throw new RuntimeException("Did not find user id - " + theId);
        }
        return theUser;
    }
    @Override
    public User save(User theUser) {
        return userRepository.save(theUser);
    }
    @Override
    public User deleteById(int theId) {
        userRepository.deleteById(theId);
        return null;
    }
}
