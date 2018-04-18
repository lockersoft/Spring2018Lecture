package com.example.davejones.spring2018lecture;

import android.app.AlertDialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class EditActivity extends BaseActivity {

  EditText edtEditName, edtEditDescription, edtAttendees;
  LiveData<Event> event;
  ImageView imageView;

  @Override
  public void onCreate( Bundle savedInstanceState ) {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.activity_edit );

    edtEditName = findViewById( R.id.edtEditName );
    edtEditDescription = findViewById( R.id.edtEditDescription );
    edtAttendees = findViewById( R.id.edtAttendees );
    imageView = findViewById( R.id.imageView );

    imageView.setImageResource( R.drawable.coming );

    // LiveData
    event = eventDatabase.eventDao().findByRecordNum( 2 );
    event.observe( this, new Observer<Event>() {

      @Override
      public void onChanged( @Nullable Event event ) {
        edtEditName.setText( event.getName() );
        edtEditDescription.setText( event.getDescription() );
        edtAttendees.setText( event.getAttendees() );
      }
    } );

  }

  public void deleteOnClick( View v ) {

    AlertDialog.Builder builder = new AlertDialog.Builder( this );
    builder.setMessage( "Are you sure you want to delete this event" )
        .setCancelable( false )
        .setPositiveButton( "Yes", new DialogInterface.OnClickListener() {
          @Override
          public void onClick( DialogInterface dialog, int which ) {
            // Perform something when they click YES
            // Delete record
            toastIt( "Record Deleted" );
          }
        } )
        .setNegativeButton( "NO", new DialogInterface.OnClickListener() {
          @Override
          public void onClick( DialogInterface dialog, int which ) {
            // Perform something when they say NO - cancel
            dialog.cancel();
            toastIt( "You pressed NO" );
          }
        } )
        .create()
        .show();
  }
}
