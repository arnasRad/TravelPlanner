package com.gmail.arnasrad.travelplanner.data;

import android.arch.lifecycle.LiveData;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.List;

import javax.inject.Inject;

public class LocationRepository {
    private final LocationDao locationDao;

    @Inject
    public LocationRepository (LocationDao locationDao) {
        this.locationDao = locationDao;
    }

    public LiveData<List<Location>> getLocationList(String travelId) {
        return locationDao.getLocationList(travelId);
    }

    public LiveData<Location> getLocation(int id) {
        return locationDao.getLocationById(id);
    }

    public Long insertLocation(Location location) {
        return locationDao.insertLocation(location);
    }

    public void updateLocation(int id, LatLngBounds latLngBounds, Place place) {
        locationDao.updateLocation(id, latLngBounds, place);
    }

    public void deleteLocation(Location location) {
        locationDao.deleteLocation(location);
    }
}
