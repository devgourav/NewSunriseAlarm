package com.beeblebroxlabs.newsunrisealarm.presentation;

import java.util.Calendar;

/**
 * Created by devgr on 17-Sep-17.
 */

public class AlarmDetailsDisplay {
  public static final long HOUR_IN_MILLIS = 1000 * 60 * 60;
  public static final long MIN_IN_MILLIS = 1000 * 60;

  private Long time;
  private Boolean isRepeating;

  public AlarmDetailsDisplay(Long time,Boolean isRepeating){
    this.time = time;
    this.isRepeating = isRepeating;
  }

  public String getAlarmDetailsText(){
    Calendar currentTime = Calendar.getInstance();
    Calendar alarmTime = Calendar.getInstance();
    alarmTime.setTimeInMillis(time);
    Long currTimeInMillis = currentTime.getTimeInMillis();
    long timeDifference, hourLeft, minuteLeft;

    if(time>=currTimeInMillis){
      timeDifference = time-currTimeInMillis;
    }
    else{
      alarmTime.set(Calendar.DATE,currentTime.get(Calendar.DATE)+1);
      timeDifference = alarmTime.getTimeInMillis()-currTimeInMillis;
    }

    hourLeft = timeDifference / HOUR_IN_MILLIS;
    timeDifference = timeDifference % HOUR_IN_MILLIS;
    minuteLeft = timeDifference / MIN_IN_MILLIS;
    String isRepeat = isRepeating?"Repeating":"Once";

    return "Alarm in "+hourLeft+" hours "+minuteLeft+" minutes " +isRepeat;

  }

}
