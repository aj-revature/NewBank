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
    private User currentUser = new User("", "");
    private List<Account> accounts = new ArrayList<>();
    private Account currentAccount = new Account();
    UserController userController = new UserController(new UserService(new UserDaoImpl()));
    AccountController accountController = new AccountController(new AccountService(new AccountDaoImpl()));
    private static final DecimalFormat df = new DecimalFormat("0.00");

    public Driver() {
    }

    public void menu() {
        Scanner scanner = new Scanner(System.in);
        if (currentUser.getUsername().isEmpty()) {
            System.out.println("Welcome to the Bank!");
        } else {
            System.out.println("Welcome to the Bank " + currentUser.getUsername() + "!");
        }
        System.out.println("Please choose below:");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("0. Exit");
        stringOption = scanner.nextLine();
        try {
            switch (stringOption) {
                case "1":
                    User credentials = getUserCredentials();
                    currentUser = userController.login(credentials);
                    while (currentUser.getUserId() == 0) {
                        credentials = getUserCredentials();
                        currentUser = userController.login(credentials);
                    }
                    accounts = accountController.getAllAccounts(currentUser);
                    currentUser.setAccounts(accounts);
                    accountCreateAndView();
                    break;
                case "2":
                    while (currentUser.getUserId() == 0) {
                        User registerCredentials = getUserCredentials();
                        currentUser = userController.registerNewUser(registerCredentials);
                    }
                    menu();
                    break;
                case "0":
                    System.out.println("Thank you for banking with us. Goodbye!");
                    break;
            }
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    public void accountCreateAndView() {
        if (!accounts.isEmpty()) {
            System.out.println("1. Create Account");
            System.out.println("2. View All Accounts");
        } else {
            System.out.println("1. Create Account");
        }
        System.out.println("0. Log Out");

        Scanner scanner = new Scanner(System.in);
        String viewOrCreate = scanner.nextLine();
        switch (viewOrCreate) {
            case "1":
                Account newAccount = getNewAccountDetails();
                currentAccount = accountController.createNewAccount(newAccount);
                accounts.add(currentAccount);
                currentUser.setAccounts(accounts);
                accountCreateAndView();
                break;
            case "2":
                selectAccount(printAccounts(currentUser));
                accountOptions();
                break;
            case "0":
                System.out.println("You are logged out. Goodbye!");
                currentUser = new User("", "");
                menu();
                break;
            default:
                accountCreateAndView();
                break;
        }

    }

    public void accountOptions() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Your current account balance is $" + df.format(currentAccount.getBalance()));
        System.out.println("What would you like to do?");
        System.out.println("1. Deposit Money");
        System.out.println("2. Withdraw Money");
        System.out.println("3. Close Account");
        System.out.println("0. Go Back");
        String choice = scanner.nextLine();
        switch (choice) {
            case "1":
                System.out.println("How much would you like to deposit?");
                numberOption = scanner.nextFloat();
                currentAccount = accountController.deposit(currentAccount, numberOption);
                accountCreateAndView();
                break;
            case "2":
                System.out.println("How much would you like to withdraw?");
                numberOption = scanner.nextFloat();
                if (currentAccount.getBalance() <= numberOption) {
                    System.out.println("Your current account balance is $" + df.format(currentAccount.getBalance()));
                    System.out.println("Please enter an amount that is less than your current balance:");
                    float numberChoice = scanner.nextFloat();
                    currentAccount = accountController.withdraw(currentAccount, numberChoice);
                } else {
                    currentAccount = accountController.withdraw(currentAccount, numberOption);
                }
                accountCreateAndView();
                break;
            case "3":
                System.out.println("Are you sure you want to delete " + currentAccount.getType()
                        + " Account #" + currentAccount.getId() +
                        " and withdraw the total balance of $" + df.format(currentAccount.getBalance()) + "? (y/n)");
                String doDelete = scanner.nextLine();
                if (doDelete.equalsIgnoreCase("y")) {
                    accountController.deleteAccount(currentAccount);
                    accounts.remove(currentAccount);
                    currentUser.setAccounts(accounts);
                }
                accountCreateAndView();
                break;
            case "0":
                accountCreateAndView();
            default:
                System.out.println("Please choose options 1, 2, or 3.");
                accountOptions();
                break;

        }
    }

    public User getUserCredentials() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter a username:");
        String username;
        String password;
        username = scanner.nextLine();
        System.out.println("Please enter a password:");
        password = scanner.nextLine();
        return new User(username, password);
    }

    public Account getNewAccountDetails() {
        Scanner scanner = new Scanner(System.in);
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

    public void selectAccount(List<Account> accounts) {
        Scanner scanner = new Scanner(System.in);
        String selectedAccount = scanner.nextLine();
        for (Account a : accounts) {
            if (selectedAccount.equals(String.valueOf(a.getId()))) {
                this.currentAccount = a;
                break;
            }
        }

    }

}


