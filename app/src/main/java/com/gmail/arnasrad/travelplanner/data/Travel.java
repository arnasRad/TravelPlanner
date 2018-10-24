package com.gmail.arnasrad.travelplanner.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Calendar;

@Entity
public class Travel {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private String Id;
    private int username;
    private String mainDestination;
    private Calendar dueDate;

    public Travel(int username, String mainDestination, Calendar dueDate) {
        this.username = username;
        this.mainDestination = mainDestination;
        this.dueDate = dueDate;
    }

    @NonNull
    public String getId() {
        return Id;
    }

    public int getUsername() {
        return username;
    }

    public String getMainDestination() {
        return mainDestination;
    }

    public void setMainDestination(String mainDestination) {
        this.mainDestination = mainDestination;
    }

    public Calendar getDueDate() {
        return dueDate;
    }

    public void setDueDate(Calendar dueDate) {
        this.dueDate = dueDate;
    }
}
