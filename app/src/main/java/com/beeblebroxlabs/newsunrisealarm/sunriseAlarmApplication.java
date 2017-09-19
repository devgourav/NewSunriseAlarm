package com.beeblebroxlabs.newsunrisealarm;

import android.app.Application;
import com.beeblebroxlabs.newsunrisealarm.storage.database.AlarmDatabase;
import com.beeblebroxlabs.newsunrisealarm.storage.database.model.Alarm;
import com.raizlabs.android.dbflow.config.DatabaseConfig;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Created by devgr on 15-Sep-17.
 */

public class sunriseAlarmApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    FlowManager.init(FlowConfig.builder(this)
        .addDatabaseConfig(DatabaseConfig.builder(AlarmDatabase.class)
            .databaseName("AlarmDatabase")
            .build())
        .build());
  }
}
