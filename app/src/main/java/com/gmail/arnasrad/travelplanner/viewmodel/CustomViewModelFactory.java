package com.gmail.arnasrad.travelplanner.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.gmail.arnasrad.travelplanner.data.AccountRepository;
import com.gmail.arnasrad.travelplanner.data.ListItemRepository;

public class CustomViewModelFactory implements ViewModelProvider.Factory {
    private final ListItemRepository listItemRepository;
    private final AccountRepository accountRepository;

    public CustomViewModelFactory(ListItemRepository listItemRepository, AccountRepository accountRepository) {
        this.listItemRepository = listItemRepository;
        this.accountRepository = accountRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ListItemCollectionViewModel.class)) {
            return (T) new ListItemCollectionViewModel(listItemRepository);
        } else if (modelClass.isAssignableFrom(ListItemViewModel.class)) {
            return (T) new ListItemViewModel(listItemRepository);
        } else if (modelClass.isAssignableFrom(NewListItemViewModel.class)) {
            return (T) new NewListItemViewModel(listItemRepository);
        } else if (modelClass.isAssignableFrom(AccountValidationViewModel.class)) {
            return (T) new AccountValidationViewModel(accountRepository);
        } else if (modelClass.isAssignableFrom(NewAccountViewModel.class)) {
            return (T) new NewAccountViewModel(accountRepository);
        } else if (modelClass.isAssignableFrom(AccountViewModel.class)) {
            return (T) new AccountViewModel(accountRepository);
        } else {
            throw new IllegalArgumentException("ViewModel Not Found");
        }
    }
}
