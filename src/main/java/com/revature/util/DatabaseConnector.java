package com.revature.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:src/main/resources/bank.db");
    }

    public static void main(String[] args) {
        try {
            try (Connection connection = getConnection()) {
                System.out.println(connection);
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
