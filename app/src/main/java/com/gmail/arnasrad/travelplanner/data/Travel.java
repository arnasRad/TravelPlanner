package com.gmail.arnasrad.travelplanner.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Calendar;

@Entity
public class Travel {
    @PrimaryKey()
    @NonNull
    private String id;
    private String username;
    private String mainDestination;
    private String startDate;
    private String endDate;
    private int colorResource;

    public Travel(@NonNull String id, String username, String mainDestination,
                  String startDate, String endDate, int colorResource) {
        this.id = id;
        this.username = username;
        this.mainDestination = mainDestination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.colorResource = colorResource;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getMainDestination() {
        return mainDestination;
    }

    public void setMainDestination(String mainDestination) {
        this.mainDestination = mainDestination;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getColorResource() {
        return colorResource;
    }

    public void setColorResource(int colorResource) {
        this.colorResource = colorResource;
    }
}
