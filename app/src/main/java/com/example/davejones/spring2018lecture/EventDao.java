package com.example.davejones.spring2018lecture;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.davejones.spring2018lecture.Event;

import java.util.List;

@Dao
public interface EventDao {

  @Query("SELECT * FROM events")
  LiveData<List<Event>> getAll();

  @Query("SELECT name FROM events")
  LiveData<List<String>> getAllNames();

  @Query("SELECT * FROM events WHERE name = :event_name LIMIT 1")
  Event findByName( String event_name );

  @Query("SELECT * FROM events WHERE eventID = :eventID")
  LiveData<Event> findByRecordNum( long eventID );

  @Insert
  void addEvent( Event event );

  @Update
  void updateEvent( Event event );

  @Delete
  void deleteEvent( Event event );
}