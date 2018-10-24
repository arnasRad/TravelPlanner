package com.gmail.arnasrad.travelplanner.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLngBounds;

@Entity
public class Location {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    private String travelId;
    private LatLngBounds latLngBounds;
    private Place place;

    public Location(String travelId, LatLngBounds latLngBounds, Place place) {
        this.travelId = travelId;
        this.latLngBounds = latLngBounds;
        this.place = place;
    }

    @NonNull
    public int getId() {
        return id;
    }

    public String getTravelId() {
        return travelId;
    }

    public LatLngBounds getLatLngBounds() {
        return latLngBounds;
    }

    public void setLatLngBounds(LatLngBounds latLngBounds) {
        this.latLngBounds = latLngBounds;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }
}
