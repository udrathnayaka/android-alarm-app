package com.example.android_alarm_app.Alarm;
import android.content.Context;
import android.content.Intent;

import android.content.BroadcastReceiver;


//Receiver for reboot
import java.util.List;

public class ReceiverActivity extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            List<Alarm> alarms = AlarmLab.get(context).getAlarms();
            for (int i = 0; i < alarms.size(); i++) {
                Alarm alarm = alarms.get(i);
                if (alarm.isOn()) {
                    alarm.scheduleAlarm(context);
                }
            }
        }
    }
}
