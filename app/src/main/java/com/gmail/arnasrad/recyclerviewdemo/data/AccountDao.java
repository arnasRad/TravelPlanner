package com.gmail.arnasrad.recyclerviewdemo.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface AccountDao {

    @Query("SELECT * FROM Account")
    LiveData<List<Account>> getAccounts();

    @Query("SELECT * FROM Account WHERE username = :username")
    LiveData<Account> getAccountByUsername(String username);

    @Query("SELECT * FROM Account WHERE password = :password")
    LiveData<Account> getAccountByPassword(String password);

    @Insert(onConflict = OnConflictStrategy.ROLLBACK)
    Long insertAccount(Account account);

    @Query("UPDATE Account SET isActive = :isActive WHERE username = :username")
    Long setActiveAccount(String username, boolean isActive);

    @Delete
    void deleteAccount(Account account);
}
