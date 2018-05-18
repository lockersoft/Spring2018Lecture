package com.example.davejones.spring2018lecture;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends BaseActivity {

  Button btnLogin;
  EditText loginName, loginPassword;

  @Override
  public void onCreate( @Nullable Bundle savedInstanceState ) {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.activity_login );
    btnLogin = findViewById( R.id.btnLogin );

    loginName = findViewById( R.id.loginName );
    loginPassword = findViewById( R.id.loginPassword );
  }

  public void loginOnClick( View v ) {
    // Save the credentials into our base activity variables
    username = loginName.getText().toString();
    password = loginPassword.getText().toString();

    // Try to login -

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

            Intent intent = new Intent( getApplicationContext(), MainActivity.class );
            startActivity( intent );
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
        Map<String, String> headers = new HashMap<String, String>();
        String credentials = username + ":" + password;
        Log.d( "AUTH", "Login Info: " + credentials );
        String auth = "Basic " + Base64.encodeToString( credentials.getBytes(), Base64.NO_WRAP );
        headers.put( "Authorization", auth );
        return headers;
      }

    };

    requestQueue.add( request );
  }
}

