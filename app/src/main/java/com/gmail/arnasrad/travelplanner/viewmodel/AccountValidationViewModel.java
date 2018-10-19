package com.gmail.arnasrad.travelplanner.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;
import android.view.View;

import com.gmail.arnasrad.travelplanner.data.Account;
import com.gmail.arnasrad.travelplanner.data.AccountRepository;
import com.gmail.arnasrad.travelplanner.login.LoginFragment;
import com.gmail.arnasrad.travelplanner.login.SignInFragment;

public class AccountValidationViewModel extends ViewModel {
    private AccountRepository repository;

    AccountValidationViewModel(AccountRepository repository) {
        this.repository = repository;
    }

    public void isAccInfoUsed(LoginFragment callback, View v, String username, String password) {
        new UserLoginValidationTask(callback, v).execute(username, password);
    }

    public void isAccInfoUsed(SignInFragment callback, View v, String username, String password) {
        new UserSignInValidationTask(callback, v).execute(username, password);
    }


    public void isPasswordUsed(LoginFragment callback, View v, Account account,
                               String username, String password) {
        new PasswordLoginValidationTask(callback, v, username, password).execute(account);
    }

    public void isPasswordUsed(SignInFragment callback, View v, Account account,
                               String username, String password) {
        new PasswordSignInValidationTask(callback, v, username, password).execute(account);
    }

    public class UserLoginValidationTask extends AsyncTask<String, Void, Account> {
        private LoginFragment mCallback;
        private View mView;
        private String username;
        private String password;

        public UserLoginValidationTask(LoginFragment mCallback, View mView) {
            this.mCallback = mCallback;
            this.mView = mView;
            this.username = "";
            this.password = "";
        }

        @Override
        protected Account doInBackground(String... strings) {
            this.username = strings[0];
            this.password = strings[1];
            return repository.getAccount(strings[0]);
        }

        @Override
        protected void onPostExecute(Account result) {
            super.onPostExecute(result);
            mCallback.onUserValidateTaskComplete(mView, result, this.username, this.password);
        }
    }

    public class UserSignInValidationTask extends AsyncTask<String, Void, Account> {
        private SignInFragment mCallback;
        private View mView;
        private String username;
        private String password;

        public UserSignInValidationTask(SignInFragment mCallback, View mView) {
            this.mCallback = mCallback;
            this.mView = mView;
            this.username = "";
            this.password = "";
        }

        @Override
        protected Account doInBackground(String... strings) {
            this.username = strings[0];
            this.password = strings[1];
            return repository.getAccount(strings[0]);
        }

        @Override
        protected void onPostExecute(Account result) {
            super.onPostExecute(result);
            mCallback.onUserValidateTaskComplete(mView, result, this.username, this.password);
        }
    }

    public class PasswordLoginValidationTask extends AsyncTask<Account, Void, Account> {
        private LoginFragment mCallback;
        private View mView;
        private String username;
        private String password;

        public PasswordLoginValidationTask(LoginFragment mCallback, View mView,
                                           String username, String password) {
            this.mCallback = mCallback;
            this.mView = mView;
            this.username = username;
            this.password = password;
        }

        @Override
        protected Account doInBackground(Account... accounts) {
            Account account = accounts[0];
            if (account != null) {
                if (account.getPassword().equals(password))
                    return account;
                else
                    return null;
            } else
                return null;
        }

        @Override
        protected void onPostExecute(Account results) {
            super.onPostExecute(results);
            if (results != null)
                mCallback.onPswValidateTaskComplete(this.mView, results,
                        results.getUsername(), this.password);
            else
                mCallback.onPswValidateTaskComplete(this.mView, results,
                        this.username, this.password);
        }
    }

    public class PasswordSignInValidationTask extends AsyncTask<Account, Void, Account> {
        private SignInFragment mCallback;
        private View mView;
        private String username;
        private String password;

        public PasswordSignInValidationTask(SignInFragment mCallback, View mView,
                                            String username, String password) {
            this.mCallback = mCallback;
            this.mView = mView;
            this.username = username;
            this.password = password;
        }

        @Override
        protected Account doInBackground(Account... accounts) {
            Account account = accounts[0];
            if (account != null) {
                if (account.getPassword().equals(password))
                    return account;
                else
                    return null;
            } else
                return null;
        }

        @Override
        protected void onPostExecute(Account results) {
            super.onPostExecute(results);
            if (results != null)
                mCallback.onPswValidateTaskComplete(
                        this.mView, results,
                        results.getUsername(), this.password);
            else
                mCallback.onPswValidateTaskComplete(
                        this.mView, results,
                        this.username, this.password);
        }
    }
}
