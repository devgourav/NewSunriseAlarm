package com.beeblebroxlabs.newsunrisealarm.storage.database;

import android.content.Context;

import com.beeblebroxlabs.newsunrisealarm.domain.model.DomainAlarm;
import com.beeblebroxlabs.newsunrisealarm.storage.database.converter.StorageModelConverter;
import com.beeblebroxlabs.newsunrisealarm.storage.database.model.Alarm;
import com.beeblebroxlabs.newsunrisealarm.storage.database.model.Alarm_Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import java.util.List;

/**
 * Created by devgr on 11-Sep-17.
 */

public class AlarmRepositoryImpl {

  private Context mContext;
  private Alarm mAlarm;

  public AlarmRepositoryImpl(Context context){
    mContext = context;
  }

  public void insert(DomainAlarm alarm){
    mAlarm = StorageModelConverter.convertToStorageModel(alarm);
    mAlarm.insert();
  }

  public void update(DomainAlarm alarm){
    mAlarm = StorageModelConverter.convertToStorageModel(alarm);
    mAlarm.update();
  }

  public void delete(DomainAlarm alarm){
    mAlarm = StorageModelConverter.convertToStorageModel(alarm);
    mAlarm.delete();
  }

  public DomainAlarm getAlarmRecord(long id){
    mAlarm = SQLite
        .select()
        .from(Alarm.class)
        .where(Alarm_Table.id.eq(id))
        .querySingle();
    return StorageModelConverter.convertToDomainModel(mAlarm);
  }

  public List<DomainAlarm> getAllAlarmRecords(long id){
    List<Alarm> alarms = SQLite
        .select()
        .from(Alarm.class)
        .queryList();

    return StorageModelConverter.convertListToDomainModel(alarms);
  }

  public void deleteAlarmRecord(long id){
    SQLite
        .delete()
        .from(Alarm.class)
        .where(Alarm_Table.id.eq(id))
        .query();
  }


}
