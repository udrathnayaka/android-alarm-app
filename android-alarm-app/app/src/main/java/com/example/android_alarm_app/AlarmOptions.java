package com.example.android_alarm_app;

import android.support.v4.app.Fragment;

import com.example.android_alarm_app.Alarm.AlarmFragmentActivity;

public class AlarmOptions extends AlarmFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return SettingsFragActivity.newInstance();
    }
}