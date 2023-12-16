package com.lcwd.user.service.services;

import com.lcwd.user.service.entities.User;

import java.util.List;

public interface UserService {
    //user operations

    //create user
    User saveUser(User user);

    //get All users
    List<User> getAllUsers();

    User getUser(String userId);
}
