package com.revature.repository;

import com.revature.entity.User;
import com.revature.exception.LoginFail;
import com.revature.exception.UserSQLException;
import com.revature.util.DatabaseConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {
    @Override
    public User createUser(User newUserCredentials) {
        String sql = "insert into `user` (username, password) values (?, ?)";
        try (Connection connection = DatabaseConnector.getConnection()) {
            PreparedStatement preppedStatement = connection.prepareStatement(sql);
            preppedStatement.setString(1, newUserCredentials.getUsername());
            preppedStatement.setString(2, newUserCredentials.getPassword());
            int result = preppedStatement.executeUpdate();
            if (result == 1) {
                newUserCredentials.setUserId(getUserIdByUsername(newUserCredentials.getUsername()).getUserId());
                System.out.println("\nNew user created.");
                return newUserCredentials;
            }
            throw new UserSQLException("User could not be created; please try again");

        } catch (SQLException e) {
            throw new UserSQLException(e.getMessage());
        }
    }

    @Override
    public User getUserIdByUsername(String username) {
        String sql = "select * from `user` where username = ?";
        try (Connection connection = DatabaseConnector.getConnection()) {
            PreparedStatement preppedStatement = connection.prepareStatement(sql);
            preppedStatement.setString(1, username);
            ResultSet resultSet = preppedStatement.executeQuery();
            if (resultSet.next()) {
                return new User(resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3));
            }
            throw new UserSQLException("User could not be retrieved; please try again");

        } catch (SQLException e) {
            throw new UserSQLException(e.getMessage());
        }
    }

    @Override
    public List<User> getAllUsers() {
        String sql = "select * from user";
        try (Connection connection = DatabaseConnector.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                User userRecord = new User(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3));
                users.add(userRecord);
            }
            return users;

        } catch (SQLException e) {
            throw new LoginFail(e.getMessage());
        }
    }
}
