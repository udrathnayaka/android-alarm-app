package com.example.android_alarm_app.Alarm;


import android.app.IntentService;
import android.content.Intent;

//Import UUID
import java.util.UUID;

//Extends IntentService
public class AlarmService extends IntentService
{


    public AlarmService() {
        super("AlarmService");
    }

    @Override
    public void onHandleIntent(Intent intent)
    {

        //Intent Implementation for question
        Intent mathActivity = new Intent(this, QuestionActivity.class);
        //Alarm Intent
        mathActivity.putExtra(Alarm.ALARM_EXTRA, (UUID) intent.getExtras().get(Alarm.ALARM_EXTRA));
        //Add flags
        mathActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //Start Question Activity
        startActivity(mathActivity);
        //Alarm Receiver method calling
        AlarmReceiver.completeWakefulIntent(intent);
    }

}
