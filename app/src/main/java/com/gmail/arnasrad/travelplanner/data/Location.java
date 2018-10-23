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
    private int Id;
    private LatLngBounds latLngBounds;
    private Place place;

    public Location(LatLngBounds latLngBounds, Place place) {
        this.latLngBounds = latLngBounds;
        this.place = place;
    }

    @NonNull
    public int getId() {
        return Id;
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
