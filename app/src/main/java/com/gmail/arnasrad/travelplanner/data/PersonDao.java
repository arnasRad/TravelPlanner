package com.gmail.arnasrad.travelplanner.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface PersonDao {
    @Query("SELECT * FROM Person WHERE travelId = :travelId")
    LiveData<List<Person>> getPersonList(String travelId);

    @Query("SELECT * FROM Person WHERE travelId = :travelId")
    List<Person> getPeopleList(String travelId);

    @Query("SELECT * FROM Person WHERE id = :id")
    LiveData<Person> getPersonById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertPerson(Person person);

    @Query("UPDATE Person SET name = :name, surname = :surname, email = :email WHERE id = :id")
    void updatePerson(int id, String name, String surname, String email);

    @Delete
    void deletePerson(Person person);
}
