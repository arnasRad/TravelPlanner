package com.gmail.arnasrad.travelplanner.login;

import android.support.v4.app.FragmentManager;
import android.os.Bundle;

import com.gmail.arnasrad.travelplanner.R;
import com.gmail.arnasrad.travelplanner.util.BaseActivity;

public class SignInActivity extends BaseActivity {
    private static final String SIGNIN_FRAG = "SIGNIN_FRAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        FragmentManager manager = getSupportFragmentManager();

        SignInFragment fragment = (SignInFragment) manager.findFragmentByTag(SIGNIN_FRAG);

        if (fragment == null) {
            fragment = SignInFragment.newInstance();
        }

        addFragmentToActivity(manager,
                fragment,
                R.id.rootActivitySignIn,
                SIGNIN_FRAG
        );
    }
}
