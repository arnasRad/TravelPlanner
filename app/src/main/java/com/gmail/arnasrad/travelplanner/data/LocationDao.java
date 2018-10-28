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
    @Query("SELECT * FROM Location WHERE travelId = :travelId AND isDestination = 0")
    LiveData<List<Location>> getLocationList(String travelId);

    @Query("SELECT * FROM Location WHERE travelId = :travelId AND isDestination = 1")
    LiveData<List<Location>> getDestinationList(String travelId);

    @Query("SELECT * FROM Location WHERE travelId = :travelId")
    List<Location> getFullLocationList(String travelId);

    @Query("SELECT * FROM Location WHERE id = :id")
    LiveData<Location> getLocationById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertLocation(Location location);

    @Query("UPDATE Location SET latitude = :latitude, longitude = :longitude, placeName = :placeName, placeAddress = :placeAddress, placeId = :placeId WHERE id = :id")
    void updateLocation(int id, double latitude, double longitude, String placeName, String placeAddress, String placeId);

    @Delete
    void deleteLocation(Location location);
}
