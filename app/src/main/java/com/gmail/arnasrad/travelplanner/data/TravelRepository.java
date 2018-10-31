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

    public LiveData<Travel> getTravelLiveData(String id) {
        return travelDao.getTravelById(id);
    }

    public Travel getTravelById(String id) {return travelDao.getTravel(id);}

    public Long insertTravel(Travel travel) {
        return travelDao.insertTravel(travel);
    }

    public void updateTravel(String id, String mainDestination, String startDate, String endDate) {
        travelDao.updateTravel(id, mainDestination, startDate, endDate);
    }

    public void deleteTravel(Travel travel) {
        travelDao.deleteTravel(travel);
    }
}
