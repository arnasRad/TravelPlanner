package com.gmail.arnasrad.travelplanner.login;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gmail.arnasrad.travelplanner.R;
import com.gmail.arnasrad.travelplanner.RoomDemoApplication;
import com.gmail.arnasrad.travelplanner.data.Account;
import com.gmail.arnasrad.travelplanner.list.ListActivity;
import com.gmail.arnasrad.travelplanner.util.ActiveAccSharedPreference;
import com.gmail.arnasrad.travelplanner.viewmodel.AccountValidationViewModel;
import com.gmail.arnasrad.travelplanner.viewmodel.AccountValidTaskCompl;

import javax.inject.Inject;

import static android.text.TextUtils.isEmpty;


public class LoginFragment extends Fragment implements AccountValidTaskCompl {
    private EditText usernameInput;
    private EditText passwordInput;
    private Button login;
    private Button signIn;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private AccountValidationViewModel accountValidationViewModel;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((RoomDemoApplication) getActivity().getApplication())
                .getApplicationComponent()
                .inject(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Set up and subscribe (observe) to the ViewModel
        accountValidationViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(AccountValidationViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        usernameInput = v.findViewById(R.id.loginUsername);
        passwordInput = v.findViewById(R.id.loginPassword);
        login = v.findViewById(R.id.loginButton);
        signIn = v.findViewById(R.id.loginSingInButton);

        EnableLoginButton();
        TextInputListener();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameInput.getText().toString();
                String password = passwordInput.getText().toString();
                // Validates account credentials
                // If credentials are correct:
                // - adds its username to shared preference
                // - starts list activity
                accountValidationViewModel.isAccInfoUsed(LoginFragment.this, v, username, password);
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignInActivity();
            }
        });


        return v;
    }

    private void startSignInActivity() {
        startActivity(new Intent(getActivity(), SignInActivity.class));
    }

    private void startListActivity() {
        startActivity(new Intent(getActivity(), ListActivity.class));
    }

    private void TextInputListener() {
        usernameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                EnableLoginButton();
            }
        });

        passwordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                EnableLoginButton();
            }
        });
    }

    private void EnableLoginButton() {
        if (isEmpty(usernameInput.getText().toString().trim()) ||
            isEmpty(passwordInput.getText().toString().trim())) {
            login.setEnabled(false);
        } else if (!login.isEnabled()) {
            login.setEnabled(true);
        }
    }

    @Override
    public void onUserValidateTaskComplete(View v, Account resultAcc,
                                           String username, String password) {
        if (resultAcc != null) {
            accountValidationViewModel.isPasswordUsed(LoginFragment.this, v, resultAcc,
                    username, password);
        } else {
            Toast.makeText(getContext(), "No such username exist", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onPswValidateTaskComplete(View v, Account resultAcc,
                                          String username, String password) {
        if (resultAcc != null) {
            ActiveAccSharedPreference.setActiveUserPreference(getContext(), username);

            Toast.makeText(getContext(), "Login successful", Toast.LENGTH_LONG).show();

            startListActivity();
        } else
            Toast.makeText(getContext(),  "Incorrect password", Toast.LENGTH_LONG).show();
    }
}
