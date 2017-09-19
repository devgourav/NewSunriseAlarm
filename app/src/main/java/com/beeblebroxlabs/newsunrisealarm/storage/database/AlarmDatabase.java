package com.beeblebroxlabs.newsunrisealarm.storage.database;

import com.raizlabs.android.dbflow.annotation.Database;

/**
* Initiliaze the Alarm Database using DBFLow Library
 */

@Database(name = AlarmDatabase.NAME, version=AlarmDatabase.VERSION)

public class AlarmDatabase {
  public static final String NAME = "Alarm_db";
  public static final int VERSION = 1;
}
