package com.example.android_alarm_app.Alarm;

import android.support.v4.app.Fragment;

public class QuestionActivity extends AlarmFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return QuestionFragment.newInstance();
    }

    //onBackPressed method
    @Override
    public void onBackPressed() {

    }
}
