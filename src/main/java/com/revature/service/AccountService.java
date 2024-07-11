package com.revature.service;

import com.revature.entity.Account;
import com.revature.entity.User;
import com.revature.repository.AccountDao;
import com.revature.repository.AccountDaoImpl;

import java.util.List;

public class AccountService {
    private AccountDaoImpl accountDao;

    public AccountService(AccountDaoImpl accountDao) {
        this.accountDao = accountDao;
    }

    public Account createAccount(Account account, User user) {
        if (checkStartingBalance(account)) {
            return accountDao.createAccount(account, user);

        }
        throw new RuntimeException();
    }

    public Account withdraw(Account account, float amountToWithdraw) {
        if(amountToWithdraw <= account.getBalance()) {
            float newBalance = account.getBalance() - amountToWithdraw;
            account.setBalance(newBalance);
            accountDao.updateAccountBalance(account);

        } else {
            System.out.println("Please enter an amount that is less than the total balance.");
        }

        return account;
    }

    public Account deposit(Account account, float amountToDeposit) {
        float newBalance = account.getBalance() + amountToDeposit;
        account.setBalance(newBalance);
        accountDao.updateAccountBalance(account);
        return account;
    }

    public boolean checkStartingBalance(Account account) {
        return account.getBalance() > 0;
    }

    public List<Account> getAllAccountsById(int userId) {
        return accountDao.getAllAccountsByUserId(userId);
    }

    public void deleteAccount(Account account) {
        accountDao.deleteAccount(account);
    }
}
