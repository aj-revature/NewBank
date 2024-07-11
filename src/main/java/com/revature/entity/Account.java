package com.revature.entity;

import java.util.Objects;

public class Account {

    int id;
    String type;
    float balance;
    int userId;

    public Account(int id, String type, float balance, int userId) {
        this.id = id;
        this.type = type;
        this.balance = balance;
        this.userId = userId;
    }

    public Account(String type, float balance, int userId) {
        this.type = type;
        this.balance = balance;
        this.userId = userId;
    }

    public Account() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return id == account.id && balance == account.balance && userId == account.userId && Objects.equals(type, account.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, balance, userId);
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", balance=" + balance +
                ", userId=" + userId +
                '}';
    }
}
