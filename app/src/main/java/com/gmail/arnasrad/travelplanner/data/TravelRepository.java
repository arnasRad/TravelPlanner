package com.gmail.arnasrad.travelplanner.data;

import android.arch.lifecycle.LiveData;

import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

public class TravelRepository {
    private final TravelDao travelDao;

    @Inject
    public TravelRepository (TravelDao travelDao) {
        this.travelDao = travelDao;
    }

    public LiveData<List<Travel>> getTravelList(String username) {
        return travelDao.getTravelList(username);
    }

    public LiveData<Travel> getTravel(String id) {
        return travelDao.getTraveById(id);
    }

    public Long insertTravel(Travel travel) {
        return travelDao.insertTravel(travel);
    }

    public void updateTravel(String id, String mainDestination, Calendar dueDate) {
        travelDao.updateTravel(id, mainDestination, dueDate);
    }

    public void deleteTravel(Travel travel) {
        travelDao.deleteTravel(travel);
    }
}
