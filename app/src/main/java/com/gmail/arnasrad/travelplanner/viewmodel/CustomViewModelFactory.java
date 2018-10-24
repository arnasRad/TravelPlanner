package com.gmail.arnasrad.travelplanner.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.gmail.arnasrad.travelplanner.data.AccountRepository;
import com.gmail.arnasrad.travelplanner.data.ListItemRepository;
import com.gmail.arnasrad.travelplanner.data.LocationRepository;
import com.gmail.arnasrad.travelplanner.data.PersonRepository;
import com.gmail.arnasrad.travelplanner.data.Travel;
import com.gmail.arnasrad.travelplanner.data.TravelRepository;

public class CustomViewModelFactory implements ViewModelProvider.Factory {
    private final ListItemRepository listItemRepository;
    private final AccountRepository accountRepository;
    private final LocationRepository locationRepository;
    private final PersonRepository personRepository;
    private final TravelRepository travelRepository;

    public CustomViewModelFactory(ListItemRepository listItemRepository,
                                  AccountRepository accountRepository,
                                  LocationRepository locationRepository,
                                  PersonRepository personRepository,
                                  TravelRepository travelRepository) {
        this.listItemRepository = listItemRepository;
        this.accountRepository = accountRepository;
        this.locationRepository = locationRepository;
        this.personRepository = personRepository;
        this.travelRepository = travelRepository;
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
        } else if (modelClass.isAssignableFrom(NewLocationViewModel.class)) {
            return (T) new NewLocationViewModel(locationRepository);
        } else if (modelClass.isAssignableFrom(NewPersonViewModel.class)) {
            return (T) new NewPersonViewModel(personRepository);
        } else if (modelClass.isAssignableFrom(NewTravelViewModel.class)) {
            return (T) new NewTravelViewModel(travelRepository);
        } else if (modelClass.isAssignableFrom(TravelCollectionViewModel.class)) {
            return (T) new TravelCollectionViewModel(travelRepository);
        } else if (modelClass.isAssignableFrom(LocationCollectionViewModel.class)) {
            return (T) new LocationCollectionViewModel(locationRepository);
        } else if (modelClass.isAssignableFrom(PersonCollectionViewModel.class)) {
            return (T) new PersonCollectionViewModel(personRepository);
        } else {
            throw new IllegalArgumentException("ViewModel Not Found");
        }
    }
}
