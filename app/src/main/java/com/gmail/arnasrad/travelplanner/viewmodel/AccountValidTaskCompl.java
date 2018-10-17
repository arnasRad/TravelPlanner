package com.gmail.arnasrad.travelplanner.viewmodel;

import android.view.View;
import com.gmail.arnasrad.travelplanner.data.Account;

public interface AccountValidTaskCompl {
    public void onUserValidateTaskComplete(View v, Account resultAcc, String username, String password);
    public void onPswValidateTaskComplete(View v, Account resultAcc, String username, String password);
}
