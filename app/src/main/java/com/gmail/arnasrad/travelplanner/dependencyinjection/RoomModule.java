/*
 * *
 *  * Copyright (C) 2017 Ryan Kay Open Source Project
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.gmail.arnasrad.travelplanner.dependencyinjection;

import android.app.Application;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.persistence.room.Room;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import com.gmail.arnasrad.travelplanner.data.AccountDao;
import com.gmail.arnasrad.travelplanner.data.AccountRepository;
import com.gmail.arnasrad.travelplanner.data.ListItemDao;
import com.gmail.arnasrad.travelplanner.data.LocationDao;
import com.gmail.arnasrad.travelplanner.data.LocationRepository;
import com.gmail.arnasrad.travelplanner.data.PersonDao;
import com.gmail.arnasrad.travelplanner.data.PersonRepository;
import com.gmail.arnasrad.travelplanner.data.TravelDao;
import com.gmail.arnasrad.travelplanner.data.TravelPlannerDatabase;
import com.gmail.arnasrad.travelplanner.data.ListItemRepository;
import com.gmail.arnasrad.travelplanner.data.TravelRepository;
import com.gmail.arnasrad.travelplanner.viewmodel.CustomViewModelFactory;

@Module
public class RoomModule {

    private final TravelPlannerDatabase database;

    public RoomModule(Application application) {
        this.database = Room.databaseBuilder(
                application,
                TravelPlannerDatabase.class,
                "ListItem.db"
        ).build();
    }

    @Provides
    @Singleton
    ListItemRepository provideListItemRepository(ListItemDao listItemDao){
        return new ListItemRepository(listItemDao);
    }

    @Provides
    @Singleton
    ListItemDao provideListItemDao(TravelPlannerDatabase database){
        return database.listItemDao();
    }

    @Provides
    @Singleton
    AccountDao provideAccountDao(TravelPlannerDatabase database){
        return database.accountDao();
    }

    @Provides
    @Singleton
    LocationDao provideLocationDao(TravelPlannerDatabase database){
        return database.locationDao();
    }

    @Provides
    @Singleton
    PersonDao providePersonDao(TravelPlannerDatabase database){
        return database.personDao();
    }

    @Provides
    @Singleton
    TravelDao provideTravelDao(TravelPlannerDatabase database){
        return database.travelDao();
    }

    @Provides
    @Singleton
    TravelPlannerDatabase provideListItemDatabase(Application application){
        return database;
    }

    @Provides
    @Singleton
    ViewModelProvider.Factory provideViewModelFactory(ListItemRepository listItemRepository,
                                                      AccountRepository accountRepository,
                                                      LocationRepository locationRepository,
                                                      PersonRepository personRepository,
                                                      TravelRepository travelRepository){
        return new CustomViewModelFactory(listItemRepository, accountRepository,
                                            locationRepository, personRepository,
                                            travelRepository);
    }
}
