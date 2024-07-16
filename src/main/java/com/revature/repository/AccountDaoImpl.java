package com.revature.repository;

import com.revature.entity.Account;
import com.revature.entity.User;
import com.revature.exception.AccountSQLException;
import com.revature.util.DatabaseConnector;

import javax.xml.crypto.Data;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class AccountDaoImpl implements AccountDao {
    private static final DecimalFormat df = new DecimalFormat("0.00");

    @Override
    public Account createAccount(Account account) {
        String sql = "insert into account (type, balance, userId) values (?, ?, ?)";

        try (Connection connection = DatabaseConnector.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, account.getType());
            stmt.setFloat(2, account.getBalance());
            stmt.setInt(3, account.getUserId());
            int result = stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (result == 1) {
                account.setId(rs.getInt(1));
                account = getAccountByAccountId(account.getId());
                System.out.println("\nAccount #" + account.getId() + " has been created and has a balance of $" +
                        df.format(account.getBalance()) + ".\n");
                return account;
            }

            throw new AccountSQLException("Account could not be created. Please try again.");
        } catch (SQLException e) {
            throw new AccountSQLException(e.getMessage());
        }
    }


    @Override
    public Account updateAccountBalance(Account account) {
        String sql = "update account set balance = ? where id = ?";

        try (Connection connection = DatabaseConnector.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setFloat(1, account.getBalance());
            stmt.setInt(2, account.getId());
            int result = stmt.executeUpdate();
            if (result == 1) {
                System.out.println("\nTransaction was successful! Your new account balance is $" + df.format(account.getBalance()) + ".\n");
                return account;
            }
            throw new AccountSQLException("Account update could not be performed. Please try again.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Account getAccountByAccountId(int accountId) {
        String sql = "select * from account where id = ?";

        try (Connection connection = DatabaseConnector.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, accountId);

            ResultSet resultSet = stmt.executeQuery();
            Account retrievedAccount = new Account();
            if (resultSet.next()) {
                retrievedAccount.setId(resultSet.getInt(1));
                retrievedAccount.setType(resultSet.getString(2));
                retrievedAccount.setBalance(Float.parseFloat(df.format(resultSet.getInt(3))));
                retrievedAccount.setUserId(resultSet.getInt(4));
            }
            return retrievedAccount;
        } catch (SQLException e) {
            throw new AccountSQLException(e.getMessage());
        }
    }

    @Override
    public List<Account> getAllAccountsByUserId(int userId) {
        String sql = "select * from account where userId = ?";

        try (Connection connection = DatabaseConnector.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, userId);

            ResultSet resultSet = stmt.executeQuery();
            List<Account> accounts = new ArrayList<>();
            while (resultSet.next()) {
                Account accountRecord = new Account();
                accountRecord.setId(resultSet.getInt(1));
                accountRecord.setType(resultSet.getString(2));
                accountRecord.setBalance(Float.parseFloat(df.format(resultSet.getInt(3))));
                accountRecord.setUserId(resultSet.getInt(4));
                accounts.add(accountRecord);
            }
            return accounts;
        } catch (SQLException e) {
            throw new AccountSQLException(e.getMessage());
        }
    }

    @Override
    public void deleteAccount(Account account) {
        String sql = "delete from account where id = ?";

        try (Connection connection = DatabaseConnector.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, account.getId());
            int result = stmt.executeUpdate();
            if (result == 1) {
                System.out.println("\nAccount #" + account.getId() + " has been deleted.\n");
            }
            else {
                throw new AccountSQLException("Account could not be deleted. Please try again");
            }
        } catch (RuntimeException | SQLException e) {
            throw new AccountSQLException(e.getMessage());
        }
    }
}
