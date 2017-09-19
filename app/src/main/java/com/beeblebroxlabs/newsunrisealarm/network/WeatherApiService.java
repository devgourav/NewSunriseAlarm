package com.beeblebroxlabs.newsunrisealarm.network;


import com.beeblebroxlabs.newsunrisealarm.network.model.pojo.CurrentWeather;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import com.beeblebroxlabs.newsunrisealarm.network.model.pojo.Weather;

/**
 * Created by devgr on 11-Sep-17.
 */

public interface WeatherApiService {

  public static final String BASE_URL = "http://api.openweathermap.org";


  @GET("/data/2.5/weather?&units=metric")
  Call<CurrentWeather> getWeatherForLocation(
      @Query("lat") Double latitude,
      @Query("lon") Double longitude,
      @Query("appid") String appId);

}
