package com.example.android_alarm_app.Alarm;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.annotation.LayoutRes;


import com.example.android_alarm_app.R;

//Fragment Implementation and Extends Activity
public abstract class AlarmFragmentActivity extends AppCompatActivity {

    protected abstract Fragment createFragment();

    //Layout
    @LayoutRes
    protected int getLayoutResId() {
        return R.layout.activity_fragment;
    }

    //onCreate Method Override
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutResId());

        //Fragment Manager Instance implementation
        FragmentManager ffragment = getSupportFragmentManager();

        //fragement
        Fragment fragment = ffragment.findFragmentById(R.id.fragment_container);

        //Condition for Checking if fragment is created.
        if (fragment == null) {
            fragment = createFragment();

            //beginTransaction method
            ffragment.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }
}
