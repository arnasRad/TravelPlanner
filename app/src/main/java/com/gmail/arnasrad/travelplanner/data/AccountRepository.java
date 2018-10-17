package com.gmail.arnasrad.travelplanner.data;

import java.util.List;

import javax.inject.Inject;

public class AccountRepository {
    private final AccountDao accountDao;

    @Inject
    public AccountRepository (AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public List<Account> getListOfAccounts() {
        return accountDao.getAccounts();
    }

    public Account getAccount(String username) {
        return accountDao.getAccountByUsername(username);
    }

    public int getUsernameCount(String username) {
        return accountDao.getUsernameCount(username);
    }

    public int getPasswordCount(String password) {
        return accountDao.getPasswordCount(password);
    }

    public void deleteAccount(Account account) {
        accountDao.deleteAccount(account);
    }

    public Long insertAccount(Account account) {
        return accountDao.insertAccount(account);
    }

    void setActiveAccount(String username, boolean isActive) {
        accountDao.setActiveAccount(username, isActive);
    }


}
