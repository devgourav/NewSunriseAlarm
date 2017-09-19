package com.beeblebroxlabs.newsunrisealarm.presentation;

import com.beeblebroxlabs.newsunrisealarm.network.model.pojo.CurrentWeather;

/**
 * Created by devgr on 17-Sep-17.
 */

public class WeatherDetailsDisplay {
  public static final String DEGREE  = "\u00b0";
  CurrentWeather currentWeather;

  public WeatherDetailsDisplay(CurrentWeather currentWeather){
    this.currentWeather = currentWeather;

  }

  public String getCurrentWeatherDetailsText(){

    Double temperature = currentWeather.getMain().getTemp();
    String city = currentWeather.getName();
    String weatherDetails = currentWeather.getWeather().get(0).getMain();

    return city + ", " + temperature + DEGREE + "C " + weatherDetails;

  }

}
