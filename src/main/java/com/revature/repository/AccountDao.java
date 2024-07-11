package com.revature.repository;

import com.revature.entity.Account;
import com.revature.entity.User;

import java.util.List;

public interface AccountDao {
    Account createAccount(Account account, User user);
    Account updateAccountBalance(Account account);
    List<Account> getAllAccountsByUserId(int userId);
    void deleteAccount(Account account);
}
