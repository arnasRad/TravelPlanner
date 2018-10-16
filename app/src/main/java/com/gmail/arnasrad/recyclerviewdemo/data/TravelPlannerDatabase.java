package com.gmail.arnasrad.recyclerviewdemo.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {ListItem.class, Account.class}, version = 1)
public abstract class TravelPlannerDatabase extends RoomDatabase {
    public abstract ListItemDao listItemDao();
    public abstract AccountDao accountDao();
}
