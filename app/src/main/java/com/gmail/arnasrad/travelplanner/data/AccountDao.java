package com.gmail.arnasrad.travelplanner.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface AccountDao {

    @Query("SELECT * FROM Account")
    List<Account> getAccounts();

    @Query("SELECT * FROM Account WHERE username = :username")
    Account getAccountByUsername(String username);

    @Query("SELECT COUNT(*) FROM Account WHERE username = :username")
    int getUsernameCount(String username);

    @Query("SELECT COUNT(*) FROM Account WHERE username = :password")
    int getPasswordCount(String password);

    @Insert(onConflict = OnConflictStrategy.ROLLBACK)
    Long insertAccount(Account account);

    @Query("UPDATE Account SET isActive = :isActive WHERE username = :username")
    void setActiveAccount(String username, boolean isActive);

    @Delete
    void deleteAccount(Account account);
}
