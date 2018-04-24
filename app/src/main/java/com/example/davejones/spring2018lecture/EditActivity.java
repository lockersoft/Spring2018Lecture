package com.example.davejones.spring2018lecture;

import android.app.AlertDialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditActivity extends BaseActivity {

  EditText edtEditName, edtEditDescription, edtAttendees;
  LiveData<Event> event;
  ImageView imageView;
  private static final int REQUEST_CAPTURE_IMAGE = 42;
  String imageFilePath;

  @Override
  public void onCreate( Bundle savedInstanceState ) {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.activity_edit );

    edtEditName = findViewById( R.id.edtEditName );
    edtEditDescription = findViewById( R.id.edtEditDescription );
    edtAttendees = findViewById( R.id.edtAttendees );
    imageView = findViewById( R.id.imageView );

    imageView.setImageResource( R.drawable.coming );
    imageView.setClickable( true );
    imageView.setOnClickListener( new View.OnClickListener() {
      @Override
      public void onClick( View v ) {
        toastIt( "Opening the camera" );
        openCameraIntentToFile();
      }
    } );

    // LiveData
    event = eventDatabase.eventDao().findByRecordNum( 2 );  // TODO:  Fix this to not be hard coded.
    event.observe( this, new Observer<Event>() {

      @Override
      public void onChanged( @Nullable Event event ) {
        edtEditName.setText( event.getName() );
        edtEditDescription.setText( event.getDescription() );
        edtAttendees.setText( event.getAttendees() );
      }
    } );
  }
  //  https://developer.android.com/training/camera/photobasics.html#TaskPath

  private File createImageFile() throws IOException {
    // Create an image file name
    String timeStamp = new SimpleDateFormat( "yyyyMMdd_HHmmss" ).format( new Date() );
    String imageFileName = "JPEG_" + timeStamp + "_";
    File storageDir = getExternalFilesDir( Environment.DIRECTORY_PICTURES );
    File image = File.createTempFile(
        imageFileName,  /* prefix */
        ".jpg",         /* suffix */
        storageDir      /* directory */
    );

    // Save a file: path for use with ACTION_VIEW intents
    imageFilePath = image.getAbsolutePath();
    return image;
  }

//  private void openCameraIntent(){
//    Intent pictureIntent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE );
//    if( pictureIntent.resolveActivity( getPackageManager()) != null ) {
//      startActivityForResult( pictureIntent, REQUEST_CAPTURE_IMAGE );
//    }
//  }

  private void openCameraIntentToFile() {
    Intent pictureIntent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE );
    if( pictureIntent.resolveActivity( getPackageManager() ) != null ) {

      File photoFile = null;
      // Create a file on the SD card to store the image data
      try {
        photoFile = createImageFile();
      } catch( IOException ex ) {
        toastIt( "File Error" );
      }

      if( photoFile != null ) {
        Uri photoURI = FileProvider.getUriForFile( this,
            "com.lockersoft.androidmobilespring2018.fileprovider", photoFile );
        pictureIntent.putExtra( MediaStore.EXTRA_OUTPUT, photoURI );
        startActivityForResult( pictureIntent, REQUEST_CAPTURE_IMAGE );
      }
    }

  }

//  @Override
//  protected void onActivityResult( int requestCode, int resultCode, Intent data ){
//    super.onActivityResult( requestCode, resultCode, data );
//
//    if( requestCode == REQUEST_CAPTURE_IMAGE &&
//        resultCode == RESULT_OK &&
//        data != null  ){
//
//      // I have an image
//      Bitmap bitmap = (Bitmap)data.getExtras().get("data");  // ONLY  a thumbnail.
//      imageView.setImageBitmap( bitmap );
//
//      // Store image in SD card, or whatever
//    }
//  }


  @Override
  protected void onActivityResult( int requestCode, int resultCode, Intent data ) {
    super.onActivityResult( requestCode, resultCode, data );

    if( requestCode == REQUEST_CAPTURE_IMAGE &&
        resultCode == RESULT_OK &&
        data != null ) {
//
//      // I have an image
//      Bitmap bitmap = (Bitmap)data.getExtras().get("data");  // ONLY  a thumbnail.
//      imageView.setImageBitmap( bitmap );
      // imageFilePath

      Bitmap bitmap = BitmapFactory.decodeFile( imageFilePath );
      imageView.setImageBitmap( bitmap );

      // Store image in SD card, or whatever
    }
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
