package com.example.android_alarm_app.Alarm;

import android.support.v4.app.Fragment;

public class AlarmActivity extends AlarmFragmentActivity {

    //Creating fragment instance
    @Override
    protected Fragment createFragment() {
        return AlarmFragment.newInstance();
    }
}

