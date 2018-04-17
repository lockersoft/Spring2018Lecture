package com.example.davejones.spring2018lecture;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * Created by dave.jones on 4/3/18.
 */

@Database(entities = { Event.class }, version = 3)
public abstract class AppDatabase extends RoomDatabase {
  public abstract EventDao eventDao();
}
