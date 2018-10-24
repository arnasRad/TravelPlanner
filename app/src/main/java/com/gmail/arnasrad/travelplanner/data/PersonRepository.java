package com.gmail.arnasrad.travelplanner.data;

import android.arch.lifecycle.LiveData;

import java.util.List;

import javax.inject.Inject;

public class PersonRepository {
    private final PersonDao personDao;

    @Inject
    public PersonRepository (PersonDao personDao) {
        this.personDao = personDao;
    }

    public LiveData<List<Person>> getPersonList(String travelId) {
        return personDao.getPersonList(travelId);
    }

    public LiveData<Person> getPerson(int id) {
        return personDao.getPersonById(id);
    }

    public Long insertPerson(Person person) {
        return personDao.insertPerson(person);
    }

    public void updatePerson(int id, String name, String surname, String email) {
        personDao.updatePerson(id, name, surname, email);
    }

    public void deletePerson(Person person) {
        personDao.deletePerson(person);
    }
}
