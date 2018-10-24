package com.gmail.arnasrad.travelplanner.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;

import com.gmail.arnasrad.travelplanner.data.Travel;
import com.gmail.arnasrad.travelplanner.data.TravelRepository;

public class NewTravelViewModel extends ViewModel {
    private TravelRepository repository;

    NewTravelViewModel(TravelRepository repository) {
        this.repository = repository;
    }

    /**
     * Attach our LiveData to the Database
     */
    public void addNewTravelToDatabase(Travel travel){
        new AddItemTask().execute(travel);
    }

    private class AddItemTask extends AsyncTask<Travel, Void, Void> {

        @Override
        protected Void doInBackground(Travel... item) {
            repository.insertTravel(item[0]);
            return null;
        }
    }
}
