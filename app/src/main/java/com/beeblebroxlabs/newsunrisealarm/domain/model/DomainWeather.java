package com.beeblebroxlabs.newsunrisealarm.domain.model;

/**
 * Created by devgr on 11-Sep-17.
 */

public class DomainWeather {

  private String temperature;
  private String place;
  private String conditions;
  private int sunriseTime;
  private String country;

  public String getTemperature() {
    return temperature;
  }

  public void setTemperature(String temperature) {
    this.temperature = temperature;
  }

  public String getPlace() {
    return place;
  }

  public void setPlace(String place) {
    this.place = place;
  }

  public String getConditions() {
    return conditions;
  }

  public void setConditions(String conditions) {
    this.conditions = conditions;
  }

  public int getSunriseTime() {
    return sunriseTime;
  }

  public void setSunriseTime(int sunriseTime) {
    this.sunriseTime = sunriseTime;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }
}
