package com.gmail.arnasrad.travelplanner.login;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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
import com.gmail.arnasrad.travelplanner.viewmodel.AccountValidationViewModel;
import com.gmail.arnasrad.travelplanner.viewmodel.NewAccountViewModel;
import com.gmail.arnasrad.travelplanner.viewmodel.AccountValidTaskCompl;

import javax.inject.Inject;

import static android.text.TextUtils.isEmpty;


public class SignInFragment extends Fragment implements AccountValidTaskCompl {
    private EditText usernameInput;
    private EditText passwordInput;
    private EditText repeatPasswordInput;
    private Button signIn;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private AccountValidationViewModel accountValidationViewModel;
    private NewAccountViewModel newAccountViewModel;

    public SignInFragment() {
        // Required empty public constructor
    }

    public static SignInFragment newInstance() {
        return new SignInFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Set up and subscribe (observe) to the ViewModel
        accountValidationViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(AccountValidationViewModel.class);
        newAccountViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(NewAccountViewModel.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((RoomDemoApplication) getActivity().getApplication())
                .getApplicationComponent()
                .inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sign_in, container, false);
        usernameInput = v.findViewById(R.id.signInUsername);
        passwordInput = v.findViewById(R.id.signInPsw);
        repeatPasswordInput = v.findViewById(R.id.signInRptPsw);
        signIn = v.findViewById(R.id.signInButton);

        EnableSignInButton();
        TextInputListener();

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameInput.getText().toString();
                String password = passwordInput.getText().toString();
                String rptPassword = repeatPasswordInput.getText().toString();
                if (password.equals(rptPassword)) {
                    accountValidationViewModel.isAccInfoUsed(SignInFragment.this, v, username, password);
                } else {
                    Toast.makeText(getContext(), "Password is not repeated correctly", Toast.LENGTH_LONG).show();
                }
            }
        });

        return v;
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
                EnableSignInButton();
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
                EnableSignInButton();
            }
        });

        repeatPasswordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                EnableSignInButton();
            }
        });
    }

    private void EnableSignInButton() {
        if (isEmpty(usernameInput.getText().toString().trim()) ||
            isEmpty(passwordInput.getText().toString().trim()) ||
            isEmpty(repeatPasswordInput.getText().toString().trim())) {
            signIn.setEnabled(false);
        } else if (!signIn.isEnabled()) {
            signIn.setEnabled(true);
        }
    }

    private void startLoginActivity() {
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }

    @Override
    public void onUserValidateTaskComplete(View v, Account resultAcc,
                                           String username, String password) {
        if (resultAcc == null) {
            accountValidationViewModel.isPasswordUsed(SignInFragment.this, v, null,
                    username, password);
        } else {
            Toast.makeText(getContext(), "Username already used", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onPswValidateTaskComplete(View v, Account resultAcc,
                                          String username, String password) {
        if (resultAcc == null) {
            newAccountViewModel.addNewAccountToDatabase(
                    new Account(username, password));
            Toast.makeText(getContext(), "User account " + username + " created successfully",
                    Toast.LENGTH_LONG).show();

            startLoginActivity();
        } else
            Toast.makeText(getContext(), "Password is already used", Toast.LENGTH_LONG).show();
    }
}
