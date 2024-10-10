package com.auction.controller;
import com.auction.entities.User;
import com.auction.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping()
    public List<User> getAllUsers () {
        return userService.findAll();
    }
    @GetMapping("/{userId}")
    public User getUserById(@PathVariable int userId) {
        User theUser = userService.findById(userId);
        return theUser;
    }
    @PostMapping("/addNewUser")
    @Transactional
    public User addNewUser(@RequestBody User theUser) {
        User newUser = userService.save(theUser);
        return newUser;
    }
    @DeleteMapping("/removeUser/{userId}")
    @Transactional
    public User deleteUser(@PathVariable int userId) {
        User theUser = userService.deleteById(userId);
        return theUser;
    }
    @PutMapping("/updateUser/{userId}")
    @Transactional
    public User updateUser(@PathVariable int userId, @RequestBody User theUser) {
        User userToUpdate = userService.findById(userId);
        if (userToUpdate != null) {
            theUser.setId(userId);
            userToUpdate = userService.save(theUser);
        }
        return userToUpdate;
    }
}
