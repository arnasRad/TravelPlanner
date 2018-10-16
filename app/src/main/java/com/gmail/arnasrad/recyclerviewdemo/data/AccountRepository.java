package com.gmail.arnasrad.recyclerviewdemo.data;

import android.arch.lifecycle.LiveData;

import java.util.List;

import javax.inject.Inject;

public class AccountRepository {
    private final AccountDao accountDao;

    @Inject
    public AccountRepository (AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public LiveData<List<Account>> getListOfAccounts() {
        return accountDao.getAccounts();
    }

    public LiveData<Account> getAccount(String username) {
        return accountDao.getAccountByUsername(username);
    }

    public LiveData<Account> getAccountByPassword(String password) {
        return accountDao.getAccountByPassword(password);
    }

    public void deleteAccount(Account account) {
        accountDao.deleteAccount(account);
    }

    public Long insertAccount(Account account) {
        return accountDao.insertAccount(account);
    }

    Long setActiveAccount(String username, boolean isActive) {
        return accountDao.setActiveAccount(username, isActive);
    }
}
