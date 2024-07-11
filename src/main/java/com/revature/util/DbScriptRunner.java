package com.revature.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Stream;

public class DbScriptRunner {

//    public void

    public static void main(String[] args) {
        Path sqlPath = Paths.get("src/main/resources/bank_setup_reset_script.sql");
        try {
            try(
                    //create connection object in try with resources block
                    Connection connection = DatabaseConnector.getConnection();
                    //create Stream that has sql lines saved as String data
                    Stream<String> lines = Files.lines(sqlPath);
            ) {
                //setting auto commit to false allows execution of multiple statements and commit all together
                //ensuring all data or no data is updated
                connection.setAutoCommit(false);
                StringBuilder sqlBuilder = new StringBuilder();
                lines.forEach(line -> sqlBuilder.append(line));
                String sql = sqlBuilder.toString();
                // \\R character is more robust new line indicator
                // references new line, carriage return, and one or two other new line characters
                String[] statements = sql.split(";\\R");
                for(String statement: statements) {
                    //for each statement we need executed, we make a statement object and call the
                    //executeUpdate method, passing in our sql String statement
                    Statement stmt = connection.createStatement();
                    stmt.executeUpdate(statement);
                }
                connection.commit();
            }
        } catch (SQLException | IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
