package com.gmail.arnasrad.travelplanner.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;

import com.gmail.arnasrad.travelplanner.data.Location;
import com.gmail.arnasrad.travelplanner.data.LocationRepository;

public class NewLocationViewModel extends ViewModel {
    private LocationRepository repository;

    NewLocationViewModel(LocationRepository repository) {
        this.repository = repository;
    }

    /**
     * Attach our LiveData to the Database
     */
    public void addNewLocationToDatabase(Location location){
        new AddItemTask().execute(location);
    }

    private class AddItemTask extends AsyncTask<Location, Void, Void> {

        @Override
        protected Void doInBackground(Location... item) {
            repository.insertLocation(item[0]);
            return null;
        }
    }
}
