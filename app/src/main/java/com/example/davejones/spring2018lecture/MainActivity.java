package com.example.davejones.spring2018lecture;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends BaseActivity {

  private EditText edtEventName;
  private EditText edtEventDescription;
  private EditText edtStartDate;
  private ListView lstViewEvents;
  ArrayAdapter adapter;
  Date startDate;
  ImageView imgPicasso;

//  LiveData<List<Event>> items;


  @Override
  public void onCreate( @Nullable Bundle savedInstanceState ) {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.activity_main );

    edtEventDescription = findViewById( R.id.edtEventDescription );
    edtEventName = findViewById( R.id.edtEventName );
    edtStartDate = findViewById( R.id.edtStartDate );
    lstViewEvents = findViewById( R.id.lstViewEvents );

    imgPicasso = findViewById( R.id.imgPicasso );
    Picasso.with( this ).load( "http://i.imgur.com/DvpvklR.png" ).into( imgPicasso );

    internetOnClick( null );

    // Create listener for the scrolling list
    lstViewEvents.setOnItemClickListener( new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick( AdapterView<?> parent, View view, int position, long id ) {
        toastIt( "You clicked on " + position + " Name: " + events[position] );
        // Switch to the Show page of the record.
        // Pass the record ID of the one that you clicked.
        Intent intent = new Intent( getApplicationContext(), EditActivity.class );
        intent.putExtra( "recordID", events[position].getId() );
        startActivity( intent );
      }
    } );
//
//    items = eventDatabase.eventDao().getAll();
//    items.observe( this, new Observer<List<Event>>() {
//      @Override
//      public void onChanged( @Nullable List<Event> eventNames ) {
//        adapter = new ArrayAdapter<Event>( getApplicationContext(), R.layout.activity_listview, eventNames );
//        lstViewEvents.setAdapter( adapter );
//        adapter.notifyDataSetChanged();
//        lstViewEvents.invalidateViews();
//        lstViewEvents.refreshDrawableState();
//      }
//    } );

  }

  public void internetOnClick( View v ) {

    String url = "https://api2018.azurewebsites.net/events";
    StringRequest request = new StringRequest(
        Request.Method.GET, url,
        // Call backs
        new Response.Listener<String>() {
          @Override
          public void onResponse( String response ) {
            // Do something with the returned data
            Log.d( "INTERNET", response );
            events = gson.fromJson( response, Event[].class );
            // Take data to display on the view
            adapter = new ArrayAdapter<Event>( getApplicationContext(), R.layout.activity_listview, events );
            lstViewEvents.setAdapter( adapter );

          }
        },
        new Response.ErrorListener() {
          @Override
          public void onErrorResponse( VolleyError error ) {
            // Do something with the error
            Log.d( "INTERNET", error.toString() );
            toastIt( "Internet Failure: " + error.toString() );
          }
        } ) {
      @Override
      public Map<String, String> getHeaders() throws AuthFailureError {
        return getCredentials();
      }

    };

    requestQueue.add( request );
  }

  public void internetOnClickSingle( View v ) {

    String url = "https://api2018.azurewebsites.net/events/1";
    StringRequest request = new StringRequest(
        Request.Method.GET, url,
        // Call backs
        new Response.Listener<String>() {
          @Override
          public void onResponse( String response ) {
            // Do something with the returned data
            Log.d( "INTERNET", response );
            Event event = gson.fromJson( response, Event.class );
            // Take data to display on the view
          }
        },
        new Response.ErrorListener() {
          @Override
          public void onErrorResponse( VolleyError error ) {
            // Do something with the error
            Log.d( "INTERNET", error.toString() );
            toastIt( "Internet Failure: " + error.toString() );
          }
        } );

    requestQueue.add( request );
  }
//
//    String url = "http://10.0.2.2:3000/todos/1";
//
//    JsonObjectRequest jsonObj = new JsonObjectRequest( Request.Method.GET, url, null,
//        // Call backs
//        new Response.Listener<JSONObject>() {
//          @Override
//          public void onResponse( JSONObject response ) {
//            // Do something with the returned data
//            Log.d( "INTERNET", response.toString());
//            // Take data to display on the view
//          }
//        },
//        new Response.ErrorListener(){
//          @Override
//          public void onErrorResponse( VolleyError error ){
//            // Do something with the error
//            Log.d( "INTERNET", error.toString());
//            toastIt( "Internet Failure: " + error.toString() );
//          }
//        }
//
//    );


  public void saveEventOnClick( View v ) {

    String url = "https://api2018.azurewebsites.net/events";
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

    // Create a JSON object first
// "name": "Daves Birthday1",
//	"description": "Cool day",
//	"attendees": "Dave, Karin",
//	"start_date": "2018-04-24T15:51:34.945Z",
//	"end_date": "2018-04-24T15:51:34.945Z"

    // HASHMAP  = HASH  Key=>Value
    HashMap<String, String> params = new HashMap<String, String>();
    params.put( "name", eventName );
    params.put( "description", eventDescription );

    // POST that JSON object to the server using VOLLEY
    JsonObjectRequest request = new JsonObjectRequest( url, new JSONObject( params ),
        new Response.Listener<JSONObject>() {
          @Override
          public void onResponse( JSONObject response ) {
            Log.d( "EVENT", response.toString() );
          }
        },
        new Response.ErrorListener() {
          @Override
          public void onErrorResponse( VolleyError error ) {
            Log.d( "EVENT", error.toString() );
          }
        }
    );

    requestQueue.add( request );
//    new Thread( new Runnable() {
//
//      @Override
//      public void run() {
//        Event event = new Event();
//        event.setName( eventName );
//        event.setDescription( eventDescription );
//        event.setAttendees( "Dave, Karin" );
//        event.setStartDate( startDate );
//        event.setEndDate( new Date() );
//        eventDatabase.eventDao().
//            addEvent( event );
//      }
//    } ).start();

  }

  public void saveEventOnClick1( View v ) {

    String url = "https://api2018.azurewebsites.net/events/" + 12;

    // POST that JSON object to the server using VOLLEY
    StringRequest request = new StringRequest( Request.Method.DELETE, url,
        new Response.Listener<String>() {
          @Override
          public void onResponse( String response ) {
            Log.d( "EVENT", response.toString() );
          }
        },
        new Response.ErrorListener() {
          @Override
          public void onErrorResponse( VolleyError error ) {
            Log.d( "EVENT", error.toString() );
          }

        }
    ) {
      @Override
      public Map<String, String> getHeaders() throws AuthFailureError {
        return getCredentials();
      }
    };

    requestQueue.add( request );
//    new Thread( new Runnable() {
//
//      @Override
//      public void run() {
//        Event event = new Event();
//        event.setName( eventName );
//        event.setDescription( eventDescription );
//        event.setAttendees( "Dave, Karin" );
//        event.setStartDate( startDate );
//        event.setEndDate( new Date() );
//        eventDatabase.eventDao().
//            addEvent( event );
//      }
//    } ).start();

  }

}
