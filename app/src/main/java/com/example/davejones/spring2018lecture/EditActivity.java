package com.example.davejones.spring2018lecture;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.widget.EditText;

public class EditActivity extends BaseActivity {

  EditText edtEditName, edtEditDescription, edtAttendees;
  LiveData<Event> event;

  @Override
  public void onCreate( Bundle savedInstanceState ) {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.activity_edit );

    edtEditName = findViewById( R.id.edtEditName );
    edtEditDescription = findViewById( R.id.edtEditDescription );
    edtAttendees = findViewById( R.id.edtAttendees );

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
}
