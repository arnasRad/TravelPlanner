package com.gmail.arnasrad.travelplanner.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;

import com.gmail.arnasrad.travelplanner.data.Account;
import com.gmail.arnasrad.travelplanner.data.AccountRepository;

public class NewAccountViewModel extends ViewModel {
    private AccountRepository repository;

    NewAccountViewModel(AccountRepository repository) {
        this.repository = repository;
    }

    public void addNewAccountToDatabase(Account account){
        new AddItemTask().execute(account);
    }

    private class AddItemTask extends AsyncTask<Account, Void, Void> {
        @Override
        protected Void doInBackground(Account... account) {
            repository.insertAccount(account[0]);
            return null;
        }
    }
}
