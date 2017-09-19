package com.beeblebroxlabs.newsunrisealarm.domain;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.beeblebroxlabs.newsunrisealarm.network.WeatherApiClient;
import com.beeblebroxlabs.newsunrisealarm.network.WeatherApiService;
import com.beeblebroxlabs.newsunrisealarm.network.model.pojo.CurrentWeather;
import com.orhanobut.hawk.Hawk;
import java.io.IOException;
import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by devgr on 12-Sep-17.
 */

public class WeatherApiUtil extends AsyncTask<Double,Void,CurrentWeather>{
  private static final String APP_ID="cf9e3211132508b56a16c068278590f0";


  private Double latitude;
  private Double longitude;
  private Context mContext;

  private static final String TAG = "WeatherApiUtil";

  public WeatherApiUtil(Context context) {
    this.mContext = context;

  }

  @Override
  protected CurrentWeather doInBackground(Double... params) {
    this.latitude = params[0];
    this.longitude = params[1];
    Hawk.init(mContext).build();

    Double mLatitude=0d;
    Double mLongitude=0d;
    if(Hawk.contains("latitude") || Hawk.contains("longitude")) {
      mLatitude = Hawk.get("latitude");
      mLongitude = Hawk.get("longitude");
    }

    if(mLatitude.doubleValue() == this.latitude.doubleValue() && mLongitude.doubleValue() == this.longitude.doubleValue()) {
      if(Hawk.contains("currentWeather")){
        return Hawk.get("currentWeather");
      }
      return null;
    }else{
      WeatherApiService weatherApiService = WeatherApiClient.getClient()
          .create(WeatherApiService.class);

      Call<CurrentWeather> currentWeatherCall = weatherApiService
          .getWeatherForLocation(latitude, longitude, APP_ID);

      HttpUrl url = currentWeatherCall.request().url();
      Log.v("Request Url:", url.toString());
      Log.v(TAG, "Weather Service called");

      try {
        Response<CurrentWeather> response = currentWeatherCall.execute();
        if (response.isSuccessful()) {
          Log.e(TAG, "Response Successful" + response.body().toString());
          if(Hawk.contains("currentWeather")){
            Hawk.delete("currentWeather");
          }
          Hawk.put("currentWeather",response.body());
          return response.body();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    return null;
  }
}
