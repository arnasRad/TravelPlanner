package com.gmail.arnasrad.travelplanner.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Person {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int Id;
    private String travelId;
    private String name;
    private String surname;
    private String email;

    public Person(String travelId, String name, String surname, String email) {
        this.travelId = travelId;
        this.name = name;
        this.surname = surname;
        this.email = email;
    }

    @NonNull
    public int getId() {
        return Id;
    }

    public String getTravelId() {
        return travelId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
