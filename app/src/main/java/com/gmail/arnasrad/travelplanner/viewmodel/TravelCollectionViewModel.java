package com.gmail.arnasrad.travelplanner.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;

import com.gmail.arnasrad.travelplanner.data.Travel;
import com.gmail.arnasrad.travelplanner.data.TravelRepository;

import java.util.List;

import javax.inject.Inject;

public class TravelCollectionViewModel extends ViewModel {

    private TravelRepository repository;

    @Inject
    public TravelCollectionViewModel(TravelRepository repository) {
        this.repository = repository;
    }

    public LiveData<List<Travel>> getTravels(String username) {
        return repository.getTravelList(username);
    }

    public void deleteTravel(Travel listItem) {
        DeleteItemTask task = new DeleteItemTask();
        task.execute(listItem);
    }

    private class DeleteItemTask extends AsyncTask<Travel, Void, Void> {
        @Override
        protected Void doInBackground(Travel... listItems) {
            repository.deleteTravel(listItems[0]);
            return null;
        }
    }

}
