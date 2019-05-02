package com.example.android_alarm_app.Alarm;



import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import java.util.UUID;


public class AlarmReceiver extends WakefulBroadcastReceiver {

    //onReceive method
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent service = new Intent(context, AlarmService.class);

        service.putExtra(Alarm.ALARM_EXTRA, (UUID) intent.getExtras().get(Alarm.ALARM_EXTRA));


        startWakefulService(context, service);
    }

}
