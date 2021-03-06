package com.example.davejones.spring2018lecture;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

public class BaseActivity extends AppCompatActivity {

  //  public AppDatabase eventDatabase;
  RequestQueue requestQueue;
  static Event[] events;
  String username, password;
  Gson gson;

  @Override
  public void onCreate( @Nullable Bundle savedInstanceState ) {//@Nullable PersistableBundle persistentState) {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.activity_base );

    username = "dave";
    password = "jones";

    GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.setDateFormat( "yyyy-MM-dd'T'HH:mm:ssX" );    //"2018-05-07T16:13:40.000Z"
    gson = gsonBuilder.create();

//    if( eventDatabase == null ) {
//      eventDatabase = Room.databaseBuilder( getApplicationContext(),
//          AppDatabase.class, "events.db" )
//          .fallbackToDestructiveMigration()
//          .build();
//    }

    // Volley Library
    Cache cache = new DiskBasedCache( getCacheDir(), 1024 * 1024 );
    Network network = new BasicNetwork( new HurlStack() );

    requestQueue = new RequestQueue( cache, network );
    requestQueue.start();
  }

  @Override
  public boolean onCreateOptionsMenu( Menu menu ) {
    // Inflate menu
    getMenuInflater().inflate( R.menu.menu, menu );
    return true;
  }

  @Override
  public boolean onOptionsItemSelected( MenuItem item ) {
    Intent intent;

    switch( item.getItemId() ) {
      case R.id.menuEdit:
        // switch to the edit activity
        intent = new Intent( this, EditActivity.class );
        startActivity( intent );
        toastIt( "You selected Edit Event" );
        return true;

      case R.id.menuViewAll:
        // switch to the view all activity
        intent = new Intent( this, MainActivity.class );
        startActivity( intent );
        return true;

      default:
        return super.onOptionsItemSelected( item );
    }
  }

  public void toastIt( String msg ) {
    Toast.makeText( getApplicationContext(),
        msg, Toast.LENGTH_SHORT ).show();
  }


  @NonNull
  public Map<String, String> getCredentials() {
    Map<String, String> headers = new HashMap<String, String>();
    String credentials = username + ":" + password;
    Log.d( "AUTH", "Login Info: " + credentials );
    String auth = "Basic " + Base64.encodeToString( credentials.getBytes(), Base64.NO_WRAP );
    headers.put( "Authorization", auth );
    return headers;
  }

}
