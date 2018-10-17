package com.gmail.arnasrad.travelplanner.login;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.gmail.arnasrad.travelplanner.R;
import com.gmail.arnasrad.travelplanner.RoomDemoApplication;
import com.gmail.arnasrad.travelplanner.data.Account;
import com.gmail.arnasrad.travelplanner.viewmodel.AccountViewModel;
import com.gmail.arnasrad.travelplanner.viewmodel.NewAccountViewModel;
import com.gmail.arnasrad.travelplanner.viewmodel.AccountValidTaskCompl;

import javax.inject.Inject;

import static android.content.Context.MODE_PRIVATE;
import static android.text.TextUtils.isEmpty;


public class SignInFragment extends Fragment implements AccountValidTaskCompl {
    private EditText usernameInput;
    private EditText passwordInput;
    private EditText repeatPasswordInput;
    private Button signIn;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private AccountViewModel accountViewModel;
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
        accountViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(AccountViewModel.class);
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
                    accountViewModel.isAccInfoUsed(SignInFragment.this, v, username, password);
                } else {
                    showSnackbar(v, "Password is not repeated correctly");
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

    private void showSnackbar(View v, String text) {
        Snackbar.make(v, text,
                Snackbar.LENGTH_SHORT)
                .setAction("Action", null)
                .show();
    }

    private void startLoginActivity() {
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }

    @Override
    public void onUserValidateTaskComplete(View v, Account resultAcc,
                                           String username, String password) {
        if (resultAcc == null) {
            accountViewModel.isPasswordUsed(SignInFragment.this, v, null,
                    username, password);
        } else {
            showSnackbar(v, "Username already used");
        }
    }

    @Override
    public void onPswValidateTaskComplete(View v, Account resultAcc,
                                          String username, String password) {
        if (resultAcc == null) {
            newAccountViewModel.addNewAccountToDatabase(
                    new Account(username, password));
            showSnackbar(v, "Account created successfully");


            Context context = getActivity();
            String sharedPreference = (context).getResources()
                    .getString(R.string.active_user_shared_preference);
            SharedPreferences.Editor editor = context.getSharedPreferences(sharedPreference, MODE_PRIVATE).edit();
            editor.putString("name", "Elena");
            editor.putInt("idName", 12);
            editor.apply();

            startLoginActivity();
        } else
            showSnackbar(v, "Password is already used");
    }
}
