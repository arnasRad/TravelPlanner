package com.gmail.arnasrad.travelplanner.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.List;

@Dao
public interface LocationDao {
    @Query("SELECT * FROM Location WHERE travelId = :travelId")
    LiveData<List<Location>> getLocationList(String travelId);

    @Query("SELECT * FROM Location WHERE id = :id")
    LiveData<Location> getLocationById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertLocation(Location location);

    @Query("UPDATE Location SET latLngBounds = :latLngBounds, place = :place WHERE id = :id")
    void updateLocation(int id, LatLngBounds latLngBounds, Place place);

    @Delete
    void deleteLocation(Location location);
}
