package com.gmail.arnasrad.travelplanner.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.gmail.arnasrad.travelplanner.data.Location;

public class MainDestinationViewModel extends ViewModel {
    private final MutableLiveData<Location> selected = new MutableLiveData<Location>();

    public void selectLocation(Location item) {
        selected.setValue(item);
    }

    public LiveData<Location> getSelectedLocation() {
        return selected;
    }

}
