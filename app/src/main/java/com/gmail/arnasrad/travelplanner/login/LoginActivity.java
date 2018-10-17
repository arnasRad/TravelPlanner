package com.gmail.arnasrad.travelplanner.login;

import android.support.v4.app.FragmentManager;
import android.os.Bundle;

import com.gmail.arnasrad.travelplanner.R;
import com.gmail.arnasrad.travelplanner.util.BaseActivity;

public class LoginActivity extends BaseActivity {
        private static final String LOGIN_FRAG = "LOGIN_FRAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FragmentManager manager = getSupportFragmentManager();

        LoginFragment fragment = (LoginFragment) manager.findFragmentByTag(LOGIN_FRAG);

        if (fragment == null) {
            fragment = LoginFragment.newInstance();
        }

        addFragmentToActivity(manager,
                fragment,
                R.id.rootActivityLogin,
                LOGIN_FRAG
        );
    }
}
