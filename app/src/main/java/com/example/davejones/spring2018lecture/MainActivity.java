package com.example.davejones.spring2018lecture;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;

import android.arch.persistence.room.Room;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends BaseActivity {

  private EditText edtEventName;
  private EditText edtEventDescription;
  private EditText edtStartDate;
  private ListView lstViewEvents;
  List<String> tempItems = new ArrayList<String>();
  ArrayAdapter adapter;
  Date startDate;

  LiveData<List<String>> items;

  @Override
  public void onCreate( Bundle savedInstanceState ) {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.activity_main );

    edtEventDescription = findViewById( R.id.edtEventDescription );
    edtEventName = findViewById( R.id.edtEventName );
    edtStartDate = findViewById( R.id.edtStartDate );
    lstViewEvents = findViewById( R.id.lstViewEvents );

    tempItems.add( "Computer" );
    tempItems.add( "Keyboard" );
    tempItems.add( "Mouse" );

    // Create array adapter for the list view to use
    adapter = new ArrayAdapter<String>( this, R.layout.activity_listview, tempItems );
    lstViewEvents.setAdapter( adapter );

    // Create listener for the scrolling list
    lstViewEvents.setOnItemClickListener( new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick( AdapterView<?> parent, View view, int position, long id ) {
        toastIt( "You clicked on " + position );
      }
    } );


    items = eventDatabase.eventDao().getAllNames();
    items.observe( this, new Observer<List<String>>() {
      @Override
      public void onChanged( @Nullable List<String> eventNames ) {
        adapter = new ArrayAdapter<String>( getApplicationContext(), R.layout.activity_listview, eventNames );
        lstViewEvents.setAdapter( adapter );
        adapter.notifyDataSetChanged();
        lstViewEvents.invalidateViews();
        lstViewEvents.refreshDrawableState();
      }

    } );

  }

  public void saveEventOnClick( View v ) {

    final String eventDescription = edtEventDescription.getText().toString();
    final String eventName = edtEventName.getText().toString();
    String startDateStr = edtStartDate.getText().toString();
    Log.d( "EVENT", "StartDate: " + startDateStr );

    SimpleDateFormat sdf = new SimpleDateFormat( "dd/mm/yyyy", Locale.ENGLISH );

    try {
      startDate = sdf.parse( startDateStr );
    } catch( ParseException e ) {
      Log.d( "EVENT", "Bad Date" );
      startDate = new Date();
    }

    new Thread( new Runnable() {

      @Override
      public void run() {
        Event event = new Event();
        event.setName( eventName );
        event.setDescription( eventDescription );
        event.setAttendees( "Dave, Karin" );
        event.setStartDate( startDate );
        event.setEndDate( new Date() );
        eventDatabase.eventDao().
            addEvent( event );
      }
    } ).start();

  }
}
