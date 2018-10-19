package com.gmail.arnasrad.travelplanner;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.gmail.arnasrad.travelplanner.data.Account;
import com.gmail.arnasrad.travelplanner.list.ListActivity;
import com.gmail.arnasrad.travelplanner.login.LoginActivity;
import com.gmail.arnasrad.travelplanner.viewmodel.AccountViewModel;
import com.gmail.arnasrad.travelplanner.viewmodel.UsernameSearchTaskComplete;

import javax.inject.Inject;

public class MainEmptyActivity extends AppCompatActivity implements UsernameSearchTaskComplete {
    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String activeUsername = getActiveUserPreference();
        if (activeUsername.equals(""))
            startLoginActivity();
        else
            startListActivity();


        /* searches database for username derived from shared preference. ERROR
        AccountViewModel accountViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(AccountViewModel.class);

        String activeUsername = getActiveUserPreference();
        if (activeUsername.equals(""))
            startLoginActivity();
        // searches in background for username in database
        // Starts Login Activity if username was not found
        accountViewModel.getUserByUsername(this, activeUsername);
        startListActivity();
        */
    }

    private void startListActivity() {
        startActivity(new Intent(this, ListActivity.class));
        finish();
    }

    private void startLoginActivity() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    public String getActiveUserPreference() {
        SharedPreferences sharedPref = this.getSharedPreferences(
                getString(R.string.active_user_preference_key), Context.MODE_PRIVATE);
        if (sharedPref != null)
            return sharedPref.getString(getString(R.string.active_user_key), null);
        return null;
    }

    @Override
    public void onUserSearchTaskComplete(Account resultAcc, String username) {
        if (resultAcc == null)
            startLoginActivity();
    }
}
