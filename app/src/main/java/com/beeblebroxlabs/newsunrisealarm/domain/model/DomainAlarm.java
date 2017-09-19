package com.beeblebroxlabs.newsunrisealarm.domain.model;

/**
 * Alarm Model
 */

public class DomainAlarm {


  private long id;
  private String label;
  private long time;
  private String tunePath;
  private Boolean repeating;

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

  public String getTunePath() {
    return tunePath;
  }

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
