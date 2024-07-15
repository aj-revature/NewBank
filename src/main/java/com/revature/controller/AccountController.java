package com.revature.controller;

import com.revature.entity.Account;
import com.revature.repository.AccountDao;
import com.revature.service.AccountService;
import com.revature.entity.User;

import java.util.List;

public class AccountController {
    AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    public Account createNewAccount(Account account) {
        return accountService.createAccount(account);

    }

    public Account deposit(Account account, float amountToDeposit) {
        return accountService.deposit(account, amountToDeposit);
    }

    public Account withdraw(Account account, float amountToWithdraw) {
        return accountService.withdraw(account, amountToWithdraw);
    }

    public List<Account> getAllAccounts(User user) {

        return accountService.getAllAccountsById(user.getUserId());
    }

    public void deleteAccount(Account account) {
        accountService.deleteAccount(account);
    }
}
