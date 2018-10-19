package com.gmail.arnasrad.travelplanner.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Person {
    @PrimaryKey
    @NonNull
    private String Id;
    private String Name;
    private String Surname;

    public Person(@NonNull String id, String name, String surname) {
        Id = id;
        Name = name;
        Surname = surname;
    }

    @NonNull
    public String getId() {
        return Id;
    }

    public void setId(@NonNull String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSurname() {
        return Surname;
    }

    public void setSurname(String surname) {
        Surname = surname;
    }
}
