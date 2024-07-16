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
            System.out.println("\nWelcome to the Bank!\n");
        } else {
            System.out.println("\nWelcome to the Bank " + currentUser.getUsername() + "!\n");
        }
        System.out.println("Please choose an option below:");
        System.out.println("\t1. Login");
        System.out.println("\t2. Register");
        System.out.println("\t0. Exit");
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
        System.out.println("\nHi " + currentUser.getUsername() + ", what would you like to do?");
        if (!accounts.isEmpty()) {
            System.out.println("\t1. Create Account");
            System.out.println("\t2. View All Accounts");
        } else {
            System.out.println("\t1. Create Account");
        }
        System.out.println("\t0. Log Out");

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
                System.out.println("You are logged out.\n");
                currentUser = new User("", "");
                menu();
                break;
            default:
                System.out.println("Please choose option 1, 2, or 0.");
                accountCreateAndView();
                break;
        }

    }

    public void accountOptions() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nYour current account balance is $" + df.format(currentAccount.getBalance()));
        System.out.println("\nWhat would you like to do?");
        System.out.println("\t1. Deposit Money");
        System.out.println("\t2. Withdraw Money");
        System.out.println("\t3. Close Account");
        System.out.println("\t0. Go Back\n");
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
                    System.out.println("Please enter a withdrawal amount that is less than your current balance:");
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
                System.out.println("Please choose option 1, 2, 3, or 0.");
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
        System.out.println("Please select the account type: \n\t1. Checking\n\t2. Savings");
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
        System.out.println("\nPlease enter the account number to view below:");
        for (Account a : accounts) {
            System.out.println("\tAccount #" + a.getId() + " " + a.getType() + ": $" + df.format(a.getBalance()));
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


