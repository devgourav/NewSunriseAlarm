package com.beeblebroxlabs.newsunrisealarm.domain;

import static com.google.android.gms.location.LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY;
import static com.google.android.gms.location.LocationRequest.PRIORITY_LOW_POWER;

import android.Manifest.permission;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.orhanobut.hawk.Hawk;

/**
 * Created by devgr on 13-Sep-17.
 */

public class LocationServiceUtil extends Service implements LocationListener,ConnectionCallbacks,OnConnectionFailedListener {

  private LocationRequest locationRequest;
  private Context mContext;
  private static final String TAG = "LocationServiceUtil";
  private GoogleApiClient googleApiClient;
  private static final int LOCATION_NORMAL_INTERVAL = 6000 * 1000;
  private static final int LOCATION_FASTEST_INTERVAL = 1500 * 1000;
  private static final int REQUEST_FINE_LOCATION = 100;
  Intent locationServiceIntent;


  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    Log.v(TAG,"Location service created");
    mContext = getApplicationContext();
    buildGoogleAPIClient();
    buildLocationRequest();
    googleApiClient.connect();
    Hawk.init(mContext).build();

  }
  private synchronized void buildGoogleAPIClient() {
    //Configuring the Google API client before connect
    this.googleApiClient = new GoogleApiClient.Builder(this)
        .addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .addApi(LocationServices.API)
        .build();
  }
  private synchronized void buildLocationRequest() {
    this.locationRequest = LocationRequest.create()
        .setInterval(LOCATION_NORMAL_INTERVAL)
        .setFastestInterval(LOCATION_FASTEST_INTERVAL)
        .setPriority(PRIORITY_LOW_POWER);
  }


  @Override
  public void onConnected(@Nullable Bundle bundle) {
    Log.e(TAG,"Google Api connected:");
    //noinspection MissingPermission
    Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
    if(location == null){
      //noinspection MissingPermission
      LocationServices.FusedLocationApi
          .requestLocationUpdates(googleApiClient, locationRequest,this);
    }else{
      handleNewLocation(location);
    }

  }

  @Override
  public void onConnectionSuspended(int i) {
    Log.e(TAG,"Google Api connection suspended:"+i);
    googleApiClient.connect();
  }

  @Override
  public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    Log.e(TAG,"Google Api connection failed:"+connectionResult.toString());
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    Log.e(TAG,"Service Destroyed");
    LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
    googleApiClient.disconnect();
  }

  @Override
  public void onLocationChanged(Location location) {
    Log.v(TAG,"Location service onLocationChanged");
    handleNewLocation(location);
  }

  public void handleNewLocation(Location location){
    Intent intent = new Intent("location_update");
    intent.putExtra("longitude",location.getLongitude());
    intent.putExtra("latitude",location.getLatitude());
    sendBroadcast(intent);
  }

}
