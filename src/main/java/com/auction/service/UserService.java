package com.auction.service;
import com.auction.entities.User;

import java.util.List;

public interface UserService{

    List<User> findAll();
    User findById(int theId);

    User save(User theUser);
    User deleteById(int theId);
    User updateUser(int userId, User user);


}
