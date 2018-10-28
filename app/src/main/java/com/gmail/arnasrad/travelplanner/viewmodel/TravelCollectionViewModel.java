package com.gmail.arnasrad.travelplanner.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;

import com.gmail.arnasrad.travelplanner.data.Travel;
import com.gmail.arnasrad.travelplanner.data.TravelRepository;

import java.util.List;

public class TravelCollectionViewModel extends ViewModel {

    private TravelRepository repository;

    public TravelCollectionViewModel(TravelRepository repository) {
        this.repository = repository;
    }

    public LiveData<List<Travel>> getTravels(String username) {
        return repository.getTravelList(username);
    }

    public Travel getTravelById(String id) {
        return repository.getTravelById(id);
    }

    public void deleteTravelById(String id) {
        DeleteTravelByIdTask task = new DeleteTravelByIdTask();
        task.execute(id);
    }

    public void deleteTravel(Travel listItem) {
        DeleteTravelTask task = new DeleteTravelTask();
        task.execute(listItem);
    }

    private class DeleteTravelTask extends AsyncTask<Travel, Void, Void> {
        @Override
        protected Void doInBackground(Travel... listItems) {
            repository.deleteTravel(listItems[0]);
            return null;
        }
    }

    private class DeleteTravelByIdTask extends AsyncTask<String, Void, Travel> {
        @Override
        protected Travel doInBackground(String ... id) {
            return getTravelById(id[0]);
        }

        @Override
        protected void onPostExecute(Travel travel) {
            super.onPostExecute(travel);
            deleteTravel(travel);
        }
    }

}
