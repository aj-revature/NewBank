package com.revature.repository;

import com.revature.entity.User;

import java.sql.SQLException;
import java.util.List;

//Data Access Object - used for facilitating direct interaction with the data persistence of choice
public interface UserDao {
    User createUser(User newUserCredentials);
    User getUserIdByUsername(String username);
    List<User> getAllUsers();
}