package com.beeblebroxlabs.newsunrisealarm.network;

import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by devgr on 11-Sep-17.
 */

public class WeatherApiClient {

  public static final String BASE_URL = "http://api.openweathermap.org";
  private static Retrofit retrofit;
  public static Retrofit getClient(){

    // Enable logging
    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

    OkHttpClient client = new OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build();

    retrofit = new Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build();

    return retrofit;
  }


}
