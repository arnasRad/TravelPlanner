package com.gmail.arnasrad.travelplanner.list;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.gmail.arnasrad.travelplanner.R;
import com.gmail.arnasrad.travelplanner.data.Account;
import com.gmail.arnasrad.travelplanner.login.LoginActivity;
import com.gmail.arnasrad.travelplanner.login.LoginFragment;
import com.gmail.arnasrad.travelplanner.util.ActiveAccSharedPreference;
import com.gmail.arnasrad.travelplanner.util.BaseActivity;
import com.gmail.arnasrad.travelplanner.viewmodel.AccountViewModel;
import com.gmail.arnasrad.travelplanner.viewmodel.UsernameSearchTaskComplete;

import javax.inject.Inject;

public class ListActivity extends BaseActivity {
    private static final String LIST_FRAG = "LIST_FRAG";
    private static final String LOGIN_FRAG = "LOGIN_FRAG";

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        if (ActiveAccSharedPreference.getActiveUserPreference(this).equals(null))
            startLoginFragment();
        else
            startListFragment();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    private void startLoginFragment() {
        startActivity(new Intent(this, LoginActivity.class));
    }

    private void startListFragment() {
        FragmentManager manager = getSupportFragmentManager();
        ListFragment fragment = (ListFragment) manager.findFragmentByTag(LIST_FRAG);

        if (fragment == null) {
            fragment = ListFragment.newInstance();
        }

        addFragmentToActivity(manager,
                fragment,
                R.id.rootActivityList,
                LIST_FRAG
        );
    }
}
