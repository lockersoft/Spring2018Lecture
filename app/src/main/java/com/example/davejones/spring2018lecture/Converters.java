package com.example.davejones.spring2018lecture;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

public class Converters {

  @TypeConverter
  public Date fromTimeStamp( Long value ) {
    return value == null ? null : new Date( value );
  }

  @TypeConverter
  public Long dateToTimestamp( Date date ) {
    return date == null ? null : date.getTime();
  }
}
