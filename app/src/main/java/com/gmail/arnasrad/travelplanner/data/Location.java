package com.gmail.arnasrad.travelplanner.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Location {
    @PrimaryKey
    @NonNull
    private String Id;
    private String streetNumber;
    private String route;
    private String locality;
    private String administrativeAreaLevel3;
    private String administrativeAreaLevel2;
    private String administrativeAreaLevel1;
    private String country;
    private String postalCode;
    private String date;

    public Location(@NonNull String id, String streetNumber, String route, String locality,
                    String administrativeAreaLevel3, String administrativeAreaLevel2,
                    String administrativeAreaLevel1, String country, String postalCode) {
        Id = id;
        this.streetNumber = streetNumber;
        this.route = route;
        this.locality = locality;
        this.administrativeAreaLevel3 = administrativeAreaLevel3;
        this.administrativeAreaLevel2 = administrativeAreaLevel2;
        this.administrativeAreaLevel1 = administrativeAreaLevel1;
        this.country = country;
        this.postalCode = postalCode;
    }

    public Location() {
        this("", "", "", "", "", "", "", "", "");
    }

    @NonNull
    public String getId() {
        return Id;
    }

    public void setId(@NonNull String id) {
        Id = id;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getAdministrativeAreaLevel3() {
        return administrativeAreaLevel3;
    }

    public void setAdministrativeAreaLevel3(String administrativeAreaLevel3) {
        this.administrativeAreaLevel3 = administrativeAreaLevel3;
    }

    public String getAdministrativeAreaLevel2() {
        return administrativeAreaLevel2;
    }

    public void setAdministrativeAreaLevel2(String administrativeAreaLevel2) {
        this.administrativeAreaLevel2 = administrativeAreaLevel2;
    }

    public String getAdministrativeAreaLevel1() {
        return administrativeAreaLevel1;
    }

    public void setAdministrativeAreaLevel1(String administrativeAreaLevel1) {
        this.administrativeAreaLevel1 = administrativeAreaLevel1;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
