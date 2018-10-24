package com.gmail.arnasrad.travelplanner.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.Calendar;
import java.util.List;

@Dao
public interface TravelDao {
    @Query("SELECT * FROM Travel WHERE username = :username")
    LiveData<List<Travel>> getTravelList(String username);

    @Query("SELECT * FROM Travel WHERE id = :id")
    LiveData<Travel> getTravelById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertTravel(Travel travel);

    @Query("UPDATE Travel SET mainDestination = :mainDestination, dueDate = :dueDate WHERE id = :id")
    void updateTravel(String id, String mainDestination, String dueDate);

    @Delete
    void deleteTravel(Travel travel);
}
