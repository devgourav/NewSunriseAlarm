
package com.beeblebroxlabs.newsunrisealarm.presentation.ui;


import android.Manifest.permission;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.beeblebroxlabs.newsunrisealarm.R;
import com.beeblebroxlabs.newsunrisealarm.presentation.AlarmDetailsDisplay;
import com.beeblebroxlabs.newsunrisealarm.domain.LocationServiceUtil;
import com.beeblebroxlabs.newsunrisealarm.domain.WeatherApiUtil;
import com.beeblebroxlabs.newsunrisealarm.network.model.pojo.CurrentWeather;
import com.beeblebroxlabs.newsunrisealarm.presentation.AlarmListAdapter;
import com.beeblebroxlabs.newsunrisealarm.presentation.WeatherDetailsDisplay;
import com.beeblebroxlabs.newsunrisealarm.presentation.ui.DeleteAlarmDialogFragment.DeleteDialogFragmentListener;
import com.beeblebroxlabs.newsunrisealarm.storage.database.AlarmDatabase;
import com.beeblebroxlabs.newsunrisealarm.storage.database.AlarmRepositoryImpl;
import com.beeblebroxlabs.newsunrisealarm.storage.database.model.Alarm;
import com.orhanobut.hawk.Hawk;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.CursorResult;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity implements DeleteDialogFragmentListener{


  private static final String TAG = "MainActivity";
  private static final int REQUEST_FINE_LOCATION = 100;
  private BroadcastReceiver broadcastReceiver;
  Intent locationServiceIntent;

  @BindView(R.id.weatherText)
  TextView weatherText;

  @BindView(R.id.alarm_recycler_view)
  RecyclerView recyclerView;
  RecyclerView.Adapter mAdapter;
  RecyclerView.LayoutManager mLayoutManager;

  List<String> alarmDetails;
  List<Alarm> alarms;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        startActivity(new Intent(MainActivity.this, SetAlarmActivity.class));
      }
    });

    startLocationService();
    getAlarmDetails();

  }

  @Override
  protected void onResume() {
    super.onResume();
    startLocationService();

    if(broadcastReceiver == null){
      broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
          Hawk.init(getApplicationContext()).build();


          Double newLatitude = intent.getExtras().getDouble("latitude");
          Double newLongitude = intent.getExtras().getDouble("longitude");

          fetchWeatherDetails(newLatitude,newLongitude);

        }
      };
      registerReceiver(broadcastReceiver,new IntentFilter("location_update"));
    }
  }

  @Override
  protected void onPause() {
    super.onPause();
    Log.v(TAG,"Service Killed");
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    stopService(locationServiceIntent);
    Log.v(TAG,"Service Killed");
    unregisterReceiver(broadcastReceiver);
  }


  private void startLocationService(){
    if ((VERSION.SDK_INT >= 23) && (
        ActivityCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED)) {
      Log.v(TAG,"Requesting perms");
      ActivityCompat.requestPermissions(this,
          new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
          REQUEST_FINE_LOCATION);
    }else{
      Log.v(TAG,"Perms granted startLocationService");
      locationServiceIntent = new Intent(getApplicationContext(), LocationServiceUtil.class);
      startService(locationServiceIntent);
    }
  }

  private void fetchWeatherDetails(Double latitude,Double longitude){
    try {
      WeatherApiUtil weatherApiUtil = new WeatherApiUtil(getApplicationContext());
      CurrentWeather weatherDetails = weatherApiUtil.execute(latitude,longitude).get();
      WeatherDetailsDisplay weatherDetailsDisplay = new WeatherDetailsDisplay(weatherDetails);
      String currWeatherDetails = weatherDetailsDisplay.getCurrentWeatherDetailsText();
      weatherText.setText(currWeatherDetails);
      if(Hawk.contains("latitude") || Hawk.contains("longitude")) {
        Hawk.delete("latitude");
        Hawk.delete("longitude");
      }
      Hawk.put("latitude",latitude);
      Hawk.put("longitude",longitude);
      Hawk.put("currentWeather",weatherDetails);
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (ExecutionException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    switch(requestCode){
      case REQUEST_FINE_LOCATION:{
        if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
          Log.v(TAG,"Perms granted");
          startLocationService();
        }else{
          Toast.makeText(this, "Location not Enabled.Weather data will not be fetched.", Toast.LENGTH_SHORT).show();
        }
      }
    }
  }
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
  }

  public void getAlarmDetails(){
    alarmDetails = new ArrayList<>();
    alarms = new ArrayList<>();

    SQLite.select()
        .from(Alarm.class)
        .async()
        .queryResultCallback(new QueryTransaction.QueryResultCallback<Alarm>() {
          @Override
          public void onQueryResult(QueryTransaction<Alarm> transaction,
              @NonNull CursorResult<Alarm> tResult) {
            alarms  = tResult.toList();
            for(Alarm alarm:alarms){
              AlarmDetailsDisplay alarmDetailsDisplay =
                  new AlarmDetailsDisplay(alarm.getTime(),alarm.getRepeating());
              alarmDetails.add(alarmDetailsDisplay.getAlarmDetailsText());
            }
          }
        }).execute();

    recyclerView.setHasFixedSize(true);
    mLayoutManager = new LinearLayoutManager(this);
    recyclerView.setLayoutManager(mLayoutManager);
    mAdapter = new AlarmListAdapter(alarmDetails);
    recyclerView.setAdapter(mAdapter);

    ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
        new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
          @Override
          public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
              RecyclerView.ViewHolder
              target) {
            return false;
          }
          @Override
          public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
            int pos = viewHolder.getAdapterPosition();
            DeleteAlarmDialogFragment deleteAlarmDialogFragment = new DeleteAlarmDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("position",pos);
            deleteAlarmDialogFragment.setArguments(bundle);
            FragmentManager manager = getSupportFragmentManager();
            deleteAlarmDialogFragment.show(manager,"DeleteAlarm");
            Log.v(TAG,"Alarm has been deleted");
          }
        };
    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
    itemTouchHelper.attachToRecyclerView(recyclerView);
  }

  @Override
  public void onClickDeleteDialogListener(int result,int pos) {
    switch (result){
      case (RESULT_OK):
        FlowManager.getDatabase(AlarmDatabase.class).getWritableDatabase();
        AlarmRepositoryImpl alarmRepositoryImpl = new AlarmRepositoryImpl(getApplicationContext());
        alarmRepositoryImpl.deleteAlarmRecord(alarms.get(pos).getId());
        alarmDetails.remove(pos);
        mAdapter.notifyItemRemoved(pos);
        break;
      case (RESULT_CANCELED):
        mAdapter.notifyDataSetChanged();
        break;
    }
  }
}
