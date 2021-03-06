package com.gmail.arnasrad.travelplanner.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;

import com.gmail.arnasrad.travelplanner.data.Person;
import com.gmail.arnasrad.travelplanner.data.PersonRepository;

import java.util.List;

import javax.inject.Inject;

public class PersonCollectionViewModel extends ViewModel {

    private PersonRepository repository;

    @Inject
    public PersonCollectionViewModel(PersonRepository repository) {
        this.repository = repository;
    }

    public LiveData<List<Person>> getPersonList(String travelId) {
        return repository.getPersonList(travelId);
    }

    public void deletePersonListByTravelId(String travelId) {
        DeletePersonListTask task = new DeletePersonListTask();
        task.execute(travelId);
    }

    public void deletePerson(Person person) {
        DeletePersonTask task = new DeletePersonTask();
        task.execute(person);
    }

    private class DeletePersonTask extends AsyncTask<Person, Void, Void> {
        @Override
        protected Void doInBackground(Person... listItems) {
            repository.deletePerson(listItems[0]);
            return null;
        }
    }

    private class DeletePersonListTask extends AsyncTask<String, Void, List<Person>> {
        @Override
        protected List<Person> doInBackground(String ... travelId) {
            return repository.getPeopleList(travelId[0]);
        }

        @Override
        protected void onPostExecute(List<Person> personList) {
            super.onPostExecute(personList);
            for(Person person: personList) {
                deletePerson(person);
            }
        }
    }
}
