package com.beeblebroxlabs.newsunrisealarm.presentation.ui;


import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import android.Manifest.permission;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import com.beeblebroxlabs.newsunrisealarm.R;
import com.beeblebroxlabs.newsunrisealarm.domain.model.DomainAlarm;
import com.beeblebroxlabs.newsunrisealarm.domain.model.RingtoneNamePathUtil;
import com.beeblebroxlabs.newsunrisealarm.network.model.pojo.CurrentWeather;
import com.beeblebroxlabs.newsunrisealarm.presentation.ui.RepeatAlarmDialogFragment.RepeatDialogFragmentListener;
import com.beeblebroxlabs.newsunrisealarm.storage.database.AlarmDatabase;
import com.beeblebroxlabs.newsunrisealarm.storage.database.AlarmRepositoryImpl;
import com.orhanobut.hawk.Hawk;
import com.raizlabs.android.dbflow.config.FlowManager;
import java.util.Calendar;

public class SetAlarmActivity extends AppCompatActivity implements RepeatDialogFragmentListener{

  private static final int CONVERT_TO_13UNIX_TIME = 1000;
  private static final int REQUEST_READ_EXTERNAL_STORAGE = 200;
  private static final String TAG = "SetAlarmActivity";

  @BindView(R.id.customTimeSwitch)
  Switch customTimeSwitch;

  @BindView(R.id.sunriseTimeSwitch)
  Switch sunriseTimeSwitch;

  @BindView(R.id.rintoneButton)
  Button rintoneButton;

  @BindView(R.id.alarmLabelEditText)
  EditText alarmLabelEditText;

  @BindView(R.id.timePicker)
  TimePicker timePicker;

  @BindView(R.id.sunriseTimeTextView)
  TextView sunriseTimeTextView;

  Intent ringtoneIntent;
  Calendar sunriseTime;
  Calendar alarmTime;
  Calendar currentTime;

  DomainAlarm alarm;
  Uri alarmUri;
  private Menu menu;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    setContentView(R.layout.activity_set_alarm);
    ButterKnife.bind(this);
    Hawk.init(getApplicationContext()).build();



    alarmTime = Calendar.getInstance();
    currentTime = Calendar.getInstance();
    timePicker.setEnabled(FALSE);
    alarm = new DomainAlarm();
    alarmUri = null;
    setSunriseTime();

    rintoneButton.setText("Choose Ringtone");
    sunriseTimeTextView.setText(sunriseTime.get(Calendar.HOUR_OF_DAY)+":"
        +sunriseTime.get(Calendar.MINUTE)+"AM");
    sunriseTimeSwitch.setText("Set sunrise time");
    customTimeSwitch.setText("Set custom time");

  }

  public void setSunriseTime(){
    sunriseTime = Calendar.getInstance();
    if(Hawk.contains("currentWeather")){
      CurrentWeather currentWeather = Hawk.get("currentWeather");
      Long sunriseTimeInMillis = currentWeather.getSys().getSunrise() * CONVERT_TO_13UNIX_TIME;
      sunriseTime.setTimeInMillis(sunriseTimeInMillis);
    }else{
      sunriseTime.set(Calendar.HOUR,6);
      sunriseTime.set(Calendar.MINUTE,0);
    }
  }


  @OnCheckedChanged(R.id.sunriseTimeSwitch)
  public void sunriseTimeSwitchListener(CompoundButton view,boolean isEnabled){
    if(isEnabled){
      sunriseTime = Calendar.getInstance();
      customTimeSwitch.setChecked(FALSE);
      timePicker.setEnabled(FALSE);
      showOptionMenu(R.id.okButton);
      showOptionMenu(R.id.cancelButton);

      alarmTime.setTimeInMillis(sunriseTime.getTimeInMillis());

      if(alarmTime.compareTo(currentTime) <=0 ){
        alarmTime.add(Calendar.DATE,1);
      }
    }else{
      hideOptionMenu(R.id.okButton);
    }
  }


  @OnCheckedChanged(R.id.customTimeSwitch)
  public void customTimeSwitchListener(CompoundButton view,boolean isEnabled){
    if(isEnabled){
      sunriseTimeSwitch.setChecked(FALSE);
      timePicker.setEnabled(TRUE);
      showOptionMenu(R.id.okButton);
      showOptionMenu(R.id.cancelButton);

      timePicker.setOnTimeChangedListener(new OnTimeChangedListener() {
        @Override
        public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
          alarmTime.set(Calendar.DATE,currentTime.get(Calendar.DATE));
          alarmTime.set(Calendar.HOUR_OF_DAY,hourOfDay);
          alarmTime.set(Calendar.MINUTE,minute);
          alarmTime.set(Calendar.SECOND, 0);
          alarmTime.set(Calendar.AM_PM,hourOfDay < 12 ? Calendar.AM : Calendar.PM);

          if(alarmTime.compareTo(currentTime) <=0 ){
            alarmTime.add(Calendar.DATE,1);
          }
        }
      });
    }else{
      timePicker.setEnabled(FALSE);
      hideOptionMenu(R.id.okButton);
    }
  }

  @OnClick(R.id.rintoneButton)
  public void onRingtoneButtonClickListener(View v){
    setRingtoneIntentData();
  }


  @SuppressLint("NewApi")
  public void setRingtoneIntentData(){
    if ((VERSION.SDK_INT >= VERSION_CODES.M) && (
        ActivityCompat.checkSelfPermission(this, permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED)) {
      Log.v(TAG,"Requesting perms");
      requestPermissions(new String[]{permission.READ_EXTERNAL_STORAGE},
          REQUEST_READ_EXTERNAL_STORAGE);
    }else{
      ringtoneIntent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
      ringtoneIntent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,RingtoneManager.TYPE_ALARM);
      ringtoneIntent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Alarm Tone");
      ringtoneIntent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI,(Uri) null);
      startActivityForResult(ringtoneIntent,REQUEST_READ_EXTERNAL_STORAGE);
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    switch(requestCode){
      case REQUEST_READ_EXTERNAL_STORAGE:{
        if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
          setRingtoneIntentData();
        }else{
          Toast.makeText(this, "Ringtone could not be Set.", Toast.LENGTH_SHORT).show();
        }
      }
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    RingtoneNamePathUtil nameUtil = new RingtoneNamePathUtil(getApplicationContext());
    String alarmToneName=" ";
    switch (requestCode) {
      case REQUEST_READ_EXTERNAL_STORAGE:
        if (resultCode == RESULT_OK) {
          alarmUri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
          if(alarmUri!=null){
            alarmToneName = nameUtil.getFileName(alarmUri);
          }
          rintoneButton.setText(alarmToneName);
        }
        break;
    }
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.set_alarm_menu, menu);
    this.menu = menu;
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch(item.getItemId()){
      case (R.id.okButton):
        if(sunriseTimeSwitch.isEnabled() || customTimeSwitch.isEnabled()){
          RepeatAlarmDialogFragment repeatAlarmDialogFragment = new RepeatAlarmDialogFragment();
          FragmentManager manager = getSupportFragmentManager();
          repeatAlarmDialogFragment.show(manager,"RepeatAlarm");
        }else{
          Toast.makeText(this, "Please select time", Toast.LENGTH_SHORT).show();
        }
        break;
      case (R.id.cancelButton):
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        break;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onClickDialogListener(int result) {
    alarm.setLabel(alarmLabelEditText.getText().toString());
    alarm.setTime(alarmTime.getTimeInMillis());

    if(alarmUri == null){
      alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
    }
    alarm.setTunePath(alarmUri.toString());

    switch (result){
      case (RESULT_OK):
        alarm.setRepeating(TRUE);
        break;
      case (RESULT_CANCELED):
        alarm.setRepeating(FALSE);
        break;
    }
    FlowManager.getDatabase(AlarmDatabase.class).getWritableDatabase();

    AlarmRepositoryImpl alarmRepositoryImpl = new AlarmRepositoryImpl(getApplicationContext());
    alarmRepositoryImpl.insert(alarm);

    Intent intent = new Intent(this, MainActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    startActivity(intent);
  }



  public void hideOptionMenu(int id){
    MenuItem item = menu.findItem(id);
    item.setVisible(FALSE);
  }


  public void showOptionMenu(int id){
    MenuItem item = menu.findItem(id);
    item.setVisible(TRUE);
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    Intent intent = new Intent(this, MainActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    startActivity(intent);
  }

}