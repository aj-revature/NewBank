package com.revature.controller;

import com.revature.entity.User;
import com.revature.service.UserService;

import java.util.Scanner;

public class UserController {
    private UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    public User registerNewUser(User userCredentials) {

        return userService.validateNewCredentials(userCredentials);
    }

    public User login(User userCredentials) {

        return userService.checkLoginCredentials(userCredentials);
    }



}
