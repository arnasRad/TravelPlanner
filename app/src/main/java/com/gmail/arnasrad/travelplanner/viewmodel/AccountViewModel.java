package com.gmail.arnasrad.travelplanner.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;
import android.view.View;

import com.gmail.arnasrad.travelplanner.MainEmptyActivity;
import com.gmail.arnasrad.travelplanner.RoomDemoApplication;
import com.gmail.arnasrad.travelplanner.data.Account;
import com.gmail.arnasrad.travelplanner.data.AccountRepository;

public class AccountViewModel extends ViewModel {
    private AccountRepository repository;

    public AccountViewModel(AccountRepository repository) {
        this.repository = repository;
    }

    public void getUserByUsername(MainEmptyActivity context, String username) {
        new UserSearchTask(context).execute(username);
    }

    public class UserSearchTask extends AsyncTask<String, Void, Account> {
        private MainEmptyActivity mCallback;
        private String username;

        public UserSearchTask(MainEmptyActivity mCallback) {
            this.mCallback = mCallback;
            this.username = "";
        }

        @Override
        protected Account doInBackground(String... strings) {
            this.username = strings[0];
            return repository.getAccount(strings[0]);
        }

        @Override
        protected void onPostExecute(Account result) {
            super.onPostExecute(result);
            mCallback.onUserSearchTaskComplete(result, this.username);
        }
    }
}
