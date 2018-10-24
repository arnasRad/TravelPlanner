package com.gmail.arnasrad.travelplanner.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {ListItem.class, Account.class,
                        Location.class, Person.class,
                        Travel.class},
        version = 1)
public abstract class TravelPlannerDatabase extends RoomDatabase {
    public abstract ListItemDao listItemDao();
    public abstract AccountDao accountDao();
    public abstract LocationDao locationDao();
    public abstract PersonDao personDao();
    public abstract TravelDao travelDao();
}
