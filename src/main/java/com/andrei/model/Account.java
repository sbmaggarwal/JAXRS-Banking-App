package com.andrei.model;

/**
 * Created by shubham on 03/12/16.
 */
public class Account {

    private String accountNumber;
    private long userId;
    private long balance;
    private String accountType;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    @Override
    public String toString() {
        return "Account{" +
                ", accountNumber='" + accountNumber + '\'' +
                ", userId=" + userId +
                ", balance=" + balance +
                ", accountType=" + accountType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        return accountNumber.equals(account.accountNumber);

    }

    @Override
    public int hashCode() {
        return accountNumber.hashCode();
    }
}
