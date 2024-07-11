package com.revature.util;

import com.revature.controller.AccountController;
import com.revature.controller.UserController;
import com.revature.entity.Account;
import com.revature.entity.User;
import com.revature.repository.AccountDaoImpl;
import com.revature.repository.UserDaoImpl;
import com.revature.service.AccountService;
import com.revature.service.UserService;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Driver {
    protected boolean continueBanking;
    private String stringOption;
    private float numberOption;
    private final Scanner scanner = new Scanner(System.in);
    private User currentUser = new User("", "");
    private List<Account> accounts = new ArrayList<>();
    private Account currentAccount = new Account();
    UserController userController = new UserController(new UserService(new UserDaoImpl()));
    AccountController accountController = new AccountController(new AccountService(new AccountDaoImpl()));
    private static final DecimalFormat df = new DecimalFormat("0.00");

    public Driver() {
    }

    public void menu() {
        System.out.println("Please choose below:");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("0. Exit");
        stringOption = scanner.nextLine();
        try {
            switch (stringOption) {
                case "1":
                    System.out.println("login");
                    User credentials = getUserCredentials();
                    currentUser = userController.login(credentials);
                    accounts = accountController.getAllAccounts(currentUser);
                    currentUser.setAccounts(accounts);
                    System.out.println(currentUser);
                    break;
                case "2":
                    System.out.println("register");
                    User registerCredentials = getUserCredentials();
                    currentUser = userController.registerNewUser(registerCredentials);
                    break;
                case "0":
                    break;
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        accountsPrompt();
    }

    public void accountsPrompt() {
        if (!accounts.isEmpty()) {
            System.out.println("1. Create Account");
            System.out.println("2. View All Accounts");
        } else {
            System.out.println("1. Create Account");
        }
        String choice = scanner.nextLine();
        switch (choice) {
            case "1":
                Account newAccount = getNewAccountDetails();
                currentAccount = accountController.createNewAccount(newAccount, currentUser);
                accounts.add(currentAccount);
                currentUser.setAccounts(accounts);
                accountsPrompt();
                break;
            case "2":
                currentAccount = selectAccount(printAccounts(currentUser));
                accountOptions();
                break;
            default:
                System.out.println("Please choose options 1. or 2.");
                accountsPrompt();
                break;
        }

    }

    public void accountOptions() {
        System.out.println("What would you like to do?");
        System.out.println("1. Deposit Money");
        System.out.println("2. Withdraw Money");
        System.out.println("3. Close Account");
        String choice = scanner.nextLine();
        switch (choice) {
            case "1":
                System.out.println("Your current account balance is $" + currentAccount.getBalance());
                System.out.println("How much would you like to deposit?");
                numberOption = scanner.nextFloat();
                currentAccount = accountController.deposit(currentAccount, numberOption);
                accountsPrompt();
                break;
            case "2":
                System.out.println("Your current account balance is $" + currentAccount.getBalance());
                System.out.println("How much would you like to withdraw?");
                numberOption = scanner.nextFloat();
                currentAccount = accountController.withdraw(currentAccount, numberOption);
                accountsPrompt();
                break;
            case "3":
                System.out.println("Are you sure you want to delete " + currentAccount.getType()
                        + " Account #" + currentAccount.getId() +
                        " and withdraw the total balance of $" + currentAccount.getBalance() + "?");
                String doDelete = scanner.nextLine();
                if (doDelete.equalsIgnoreCase("y")) {
                    accountController.deleteAccount(currentAccount);
                }
                accountsPrompt();
                break;
            default:
                System.out.println("Please choose options 1, 2, or 3.");
                accountsPrompt();
                break;

        }
    }

    public User getUserCredentials() {
        System.out.println("Please enter a username:");
        String username;
        String password;
        username = scanner.nextLine();
        System.out.println("Please enter a password");
        password = scanner.nextLine();
        return new User(username, password);
    }

    public Account getNewAccountDetails() {
        System.out.println("Please select the account type: \n1. Checking\n2. Savings");
        String typeChoice = scanner.nextLine();
        System.out.println("Please enter the initial deposit amount: ");
        float balance = scanner.nextFloat();
        Account accountInfo;
        if (typeChoice.equals("1")) {
            accountInfo = new Account("Checking", balance, currentUser.getUserId());
        } else {
            accountInfo = new Account("Savings", balance, currentUser.getUserId());
        }
        return accountInfo;
    }

    public List<Account> printAccounts(User user) {
        System.out.println("Please enter the account number to view below:");
        for (Account a : accounts) {
            System.out.println("Account #" + a.getId() + " " + a.getType() + ": $" + df.format(a.getBalance()));
        }
        return accounts;
    }

    public Account selectAccount(List<Account> accounts) {
        String selectedAccount = scanner.nextLine();
        Account chosenAccount = new Account();
        for (Account a : accounts) {
            if (selectedAccount.equals(String.valueOf(a.getId()))) {
                chosenAccount = a;
            }
        }
        return chosenAccount;
    }

}


