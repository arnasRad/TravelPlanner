package com.gmail.arnasrad.travelplanner.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;

import com.gmail.arnasrad.travelplanner.data.Person;
import com.gmail.arnasrad.travelplanner.data.PersonRepository;

public class NewPersonViewModel extends ViewModel {
    private PersonRepository repository;

    NewPersonViewModel(PersonRepository repository) {
        this.repository = repository;
    }

    /**
     * Attach our LiveData to the Database
     */
    public void addNewPersonToDatabase(Person person){
        new AddItemTask().execute(person);
    }

    private class AddItemTask extends AsyncTask<Person, Void, Void> {

        @Override
        protected Void doInBackground(Person... item) {
            repository.insertPerson(item[0]);
            return null;
        }
    }
}
