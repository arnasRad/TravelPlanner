package com.gmail.arnasrad.travelplanner.viewmodel;

import android.view.View;

import com.gmail.arnasrad.travelplanner.data.Account;

public interface UsernameSearchTaskComplete {
    void onUserSearchTaskComplete(Account resultAcc, String username);
}
