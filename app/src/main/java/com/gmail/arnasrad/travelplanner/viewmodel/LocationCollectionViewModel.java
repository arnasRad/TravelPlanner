package com.gmail.arnasrad.travelplanner.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;

import com.gmail.arnasrad.travelplanner.data.Location;
import com.gmail.arnasrad.travelplanner.data.LocationRepository;

import java.util.List;

import javax.inject.Inject;

public class LocationCollectionViewModel extends ViewModel {
    private LocationRepository repository;

    @Inject
    public LocationCollectionViewModel(LocationRepository repository) {
        this.repository = repository;
    }

    public LiveData<List<Location>> getLocationList(String travelId) {
        return repository.getLocationList(travelId);
    }

    public LiveData<List<Location>> getDestinationList(String travelId) {
        return repository.getDestinationList(travelId);
    }

    public void deleteLocationsByTravelId(String travelId) {
        DeleteLocationListTask task = new DeleteLocationListTask();
        task.execute(travelId);
    }

    public void deleteLocation(Location location) {
        DeleteLocationTask task = new DeleteLocationTask();
        task.execute(location);
    }

    private class DeleteLocationTask extends AsyncTask<Location, Void, Void> {
        @Override
        protected Void doInBackground(Location... listItems) {
            repository.deleteLocation(listItems[0]);
            return null;
        }
    }

    private class DeleteLocationListTask extends AsyncTask<String, Void, List<Location>> {
        @Override
        protected List<Location> doInBackground(String ... travelId) {
            return repository.getFullLocationList(travelId[0]);
        }

        @Override
        protected void onPostExecute(List<Location> locations) {
            super.onPostExecute(locations);
            for(Location loc: locations) {
                deleteLocation(loc);
            }
        }
    }
}
