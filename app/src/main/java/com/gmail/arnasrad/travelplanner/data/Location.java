package com.gmail.arnasrad.travelplanner.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.List;

@Entity
public class Location {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    private String travelId;
    private double latitude;
    private double longitude;
    private String placeName;
    private String placeAddress;
    private String placeId;
    private boolean isDestination;

    public Location(String travelId, LatLngBounds latLngBounds, Place place, boolean isDestination) {
        this.travelId = travelId;
        this.latitude = latLngBounds.getCenter().latitude;
        this.longitude = latLngBounds.getCenter().longitude;
        this.placeName = place.getName().toString();
        this.placeAddress = place.getAddress().toString();
        this.placeId = place.getId();
        this.isDestination = isDestination;
    }

    public Location(String travelId, double latitude, double longitude, String placeName, String placeAddress, String placeId, boolean isDestination) {
        this.travelId = travelId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.placeName = placeName;
        this.placeAddress = placeAddress;
        this.placeId = placeId;
        this.isDestination = isDestination;
    }

    @NonNull
    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    public String getTravelId() {
        return travelId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getPlaceAddress() {
        return placeAddress;
    }

    public void setPlaceAddress(String placeAddress) {
        this.placeAddress = placeAddress;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public boolean isDestination() {
        return isDestination;
    }
}
