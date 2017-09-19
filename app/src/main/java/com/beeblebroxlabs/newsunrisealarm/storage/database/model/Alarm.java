package com.beeblebroxlabs.newsunrisealarm.storage.database.model;

import com.beeblebroxlabs.newsunrisealarm.storage.database.AlarmDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Initialize Alarm Table using SbFlow library
 */


@Table(database = AlarmDatabase.class)
public class Alarm extends BaseModel{

  @PrimaryKey(autoincrement = true)
  private long id;

  @Column
  private String label;

  @Column
  private long time;

  @Column
  private String tunePath;

  @Column(getterName = "getRepeating")
  private Boolean repeating;

  public Alarm(){

  }
  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public long getTime() {
    return time;
  }

  public void setTime(long time) {
    this.time = time;
  }

  public String getTunePath() {return tunePath;}

  public void setTunePath(String tunePath) {
    this.tunePath = tunePath;
  }

  public Boolean getRepeating() {
    return repeating;
  }

  public void setRepeating(Boolean repeating) {
    this.repeating = repeating;
  }
}
