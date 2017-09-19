package com.beeblebroxlabs.newsunrisealarm.storage.database.converter;


import com.beeblebroxlabs.newsunrisealarm.domain.model.DomainAlarm;
import com.beeblebroxlabs.newsunrisealarm.storage.database.model.Alarm;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by devgr on 11-Sep-17.
 */

public class StorageModelConverter {

  public static Alarm convertToStorageModel(DomainAlarm alarmModel){
    Alarm result = new Alarm();
    result.setId(alarmModel.getId());
    result.setLabel(alarmModel.getLabel());
    result.setTime(alarmModel.getTime());
    result.setTunePath(alarmModel.getTunePath());
    result.setRepeating(alarmModel.getRepeating());
    return  result;
  }

  public static DomainAlarm convertToDomainModel(Alarm alarmModel){
    DomainAlarm result = new DomainAlarm();
    result.setId(alarmModel.getId());
    result.setLabel(alarmModel.getLabel());
    result.setTime(alarmModel.getTime());
    result.setTunePath(alarmModel.getTunePath());
    result.setRepeating(alarmModel.getRepeating());
    return  result;
  }

  public static List<DomainAlarm> convertListToDomainModel(List<Alarm> alarms) {
    List<DomainAlarm> convertedAlarms = new ArrayList<>();

    for (Alarm alarm : alarms) {
      convertedAlarms.add(convertToDomainModel(alarm));
    }

    alarms.clear();
    return convertedAlarms;
  }

}
