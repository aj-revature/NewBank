package com.revature.service;

import com.revature.entity.User;
import com.revature.exception.LoginFail;
import com.revature.repository.UserDao;

import java.util.List;

public class UserService {
    UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User validateNewCredentials(User newUserCredentials) {
        //1. check if lengths are correct
        if (checkUsernameLength(newUserCredentials)) {
            if (checkPasswordLength(newUserCredentials)) {
                //2. check if unique
                if (checkUsernameIsUnique(newUserCredentials)) {

                    return userDao.createUser(newUserCredentials);
                }
                else {
                    System.out.println("Username " + newUserCredentials.getUsername() + " already exists." +
                            " Please choose a unique username.");
                }
            }
            else {
                System.out.println("Password is the wrong length." +
                        " Please choose a password that is between 1 and 30 characters long.");
            }

        } else {
            System.out.println("Username " + newUserCredentials.getUsername() + " is the wrong length." +
                    " Please choose a username that is between 1 and 30 characters long.");
        }
        return newUserCredentials;
    }

    public User checkLoginCredentials(User credentials) {
        for (User user : userDao.getAllUsers()) {
            boolean usernameMatches = user.getUsername().equals(credentials.getUsername());
            boolean passwordMatch = user.getPassword().equals(credentials.getPassword());
            if (usernameMatches && passwordMatch) {
                System.out.println("Hi, " + user.getUsername() + ". You are logged in!");
                credentials.setUserId(user.getUserId());
            }
        }
        if (credentials.getUserId() == 0) {
            System.out.println("Credentials are incorrect. Please try again.");
        }
        return credentials;
    }

    private boolean checkUsernameLength(User userCredentials) {

        return userCredentials.getUsername().length() <= 30 &&
                !userCredentials.getUsername().isEmpty();
    }

    private boolean checkPasswordLength(User userCredentials) {
        return userCredentials.getPassword().length() <= 30 &&
                !userCredentials.getPassword().isEmpty();
    }

    private boolean checkUsernameIsUnique(User userCredentials) {
        List<User> users = userDao.getAllUsers();
        boolean usernameIsUnique = true;
        for (User user : users) {
            if(userCredentials.getUsername().equals(user.getUsername())) {
                usernameIsUnique = false;
                break;
            }
        }
        return usernameIsUnique;
    }
}
