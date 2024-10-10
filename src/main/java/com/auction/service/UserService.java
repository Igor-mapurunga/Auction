package com.auction.service;

import com.auction.dao.UserRepository;
import com.auction.entities.User;

import java.util.List;

public interface UserService{

    List<User> findAll();
    User findById(int theId);

    User save(User theUser);

    User deleteById(int theId);

}
