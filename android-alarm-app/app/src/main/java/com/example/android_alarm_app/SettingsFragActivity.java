package com.example.android_alarm_app;

import com.example.android_alarm_app.Alarm.Alarm;
import com.example.android_alarm_app.Alarm.AlarmFragment;
import com.example.android_alarm_app.Alarm.AlarmLab;
import com.example.android_alarm_app.Alarm.QuestionActivity;
import com.example.android_alarm_app.TimePicker.TimePickerFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;



public class SettingsFragActivity extends Fragment {

    TextView mTime;
    ToggleButton mRSunTButton;
    ToggleButton mRMonTButton;
    ToggleButton mRTueTButton;
    ToggleButton mRWedTButton;
    ToggleButton mRThuTButton;
    ToggleButton mRFriTButton;
    ToggleButton mRSatTButton;
    Switch mRepeatSwitch;
    Spinner mDifficultySpinner;
    Spinner mToneSpinner;
    EditText mSnoozeText;
    Switch mVibrateSwitch;
    Button mTestButton;

    AlarmLab mAlarmLab;
    Alarm mAlarm;
    Alarm mTestAlarm;
    String mId, mRepeat;
    boolean mAdd = false;

    Uri[] mAlarmTones;

    private static final int REQUEST_TIME = 0;
    private static final String DIALOG_TIME = "DialogTime";
    private static final int REQUEST_TEST = 1;


    private static final String TAG = "AlarmSettings";

    public static SettingsFragActivity newInstance() {
        return new SettingsFragActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_alarm_settings, parent, false);

        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            mId = extras.getString(AlarmFragment.GET_ALARM);
        }

        mAlarmLab = AlarmLab.get(getActivity());

        if (!mId.isEmpty()) {
            mAlarm = mAlarmLab.getAlarm(UUID.fromString(mId));
        } else {
            mAlarm = new Alarm();
            Calendar cal = Calendar.getInstance();

            mAlarm.setHour(cal.get(Calendar.HOUR_OF_DAY));
            mAlarm.setMinute(cal.get(Calendar.MINUTE));
            mAdd = true;
        }

        if (savedInstanceState != null) {
            mAlarm.setHour(savedInstanceState.getInt("hour", 0));
            mAlarm.setMinute(savedInstanceState.getInt("minute", 0));
            mAlarm.setRepeatDays(savedInstanceState.getString("repeat"));
            mAlarm.setRepeat(savedInstanceState.getBoolean("repeatweekly"));
        }

        mRepeat = mAlarm.getRepeat();

        mTime = (TextView) v.findViewById(R.id.settings_time);
        mTime.setText(mAlarm.getFormatTime());
        mTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.FragmentManager manager = getFragmentManager();
                TimePickerFragment dialog = TimePickerFragment
                        .newInstance(mAlarm.getHour(), mAlarm.getMinute());
                dialog.setTargetFragment(SettingsFragActivity.this, REQUEST_TIME);
                dialog.show(manager, DIALOG_TIME);
            }
        });

        mRSunTButton = (ToggleButton) v.findViewById(R.id.set_repeat_sun);
        mRSunTButton.setChecked(mRepeat.charAt(Alarm.SUN) == 'T');
        mRSunTButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRSunTButton.setChecked(mRepeat.charAt(Alarm.SUN) != 'T');

                StringBuilder sb = new StringBuilder(mRepeat);
                if (mRepeat.charAt(Alarm.SUN) == 'T') {
                    sb.setCharAt(Alarm.SUN, 'F');
                    mRepeat = sb.toString();
                    mAlarm.setRepeatDays(mRepeat);
                } else {
                    sb.setCharAt(Alarm.SUN, 'T');
                    mRepeat = sb.toString();
                    mAlarm.setRepeatDays(mRepeat);
                }
            }
        });

        mRMonTButton = (ToggleButton) v.findViewById(R.id.set_repeat_mon);
        mRMonTButton.setChecked(mRepeat.charAt(Alarm.MON) == 'T');
        mRMonTButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRMonTButton.setChecked(mRepeat.charAt(Alarm.MON) != 'T');

                StringBuilder sb = new StringBuilder(mRepeat);
                if (mRepeat.charAt(Alarm.MON) == 'T') {
                    sb.setCharAt(Alarm.MON, 'F');
                    mRepeat = sb.toString();
                    mAlarm.setRepeatDays(mRepeat);
                } else {
                    sb.setCharAt(Alarm.MON, 'T');
                    mRepeat = sb.toString();
                    mAlarm.setRepeatDays(mRepeat);
                }
            }
        });

        mRTueTButton = (ToggleButton) v.findViewById(R.id.set_repeat_tue);
        mRTueTButton.setChecked(mRepeat.charAt(Alarm.TUE) == 'T');
        mRTueTButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRTueTButton.setChecked(mRepeat.charAt(Alarm.TUE) != 'T');

                StringBuilder sb = new StringBuilder(mRepeat);
                if (mRepeat.charAt(Alarm.TUE) == 'T') {
                    sb.setCharAt(Alarm.TUE, 'F');
                    mRepeat = sb.toString();
                    mAlarm.setRepeatDays(mRepeat);
                } else {
                    sb.setCharAt(Alarm.TUE, 'T');
                    mRepeat = sb.toString();
                    mAlarm.setRepeatDays(mRepeat);
                }
            }
        });

        mRWedTButton = (ToggleButton) v.findViewById(R.id.set_repeat_wed);
        mRWedTButton.setChecked(mRepeat.charAt(Alarm.WED) == 'T');
        mRWedTButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRWedTButton.setChecked(mRepeat.charAt(Alarm.WED) != 'T');

                StringBuilder sb = new StringBuilder(mRepeat);
                if (mRepeat.charAt(Alarm.WED) == 'T') {
                    sb.setCharAt(Alarm.WED, 'F');
                    mRepeat = sb.toString();
                    mAlarm.setRepeatDays(mRepeat);
                } else {
                    sb.setCharAt(Alarm.WED, 'T');
                    mRepeat = sb.toString();
                    mAlarm.setRepeatDays(mRepeat);
                }
            }
        });

        mRThuTButton = (ToggleButton) v.findViewById(R.id.set_repeat_thu);
        mRThuTButton.setChecked(mRepeat.charAt(Alarm.THU) == 'T');
        mRThuTButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRThuTButton.setChecked(mRepeat.charAt(Alarm.THU) != 'T');

                StringBuilder sb = new StringBuilder(mRepeat);
                if (mRepeat.charAt(Alarm.THU) == 'T') {
                    sb.setCharAt(Alarm.THU, 'F');
                    mRepeat = sb.toString();
                    mAlarm.setRepeatDays(mRepeat);
                } else {
                    sb.setCharAt(Alarm.THU, 'T');
                    mRepeat = sb.toString();
                    mAlarm.setRepeatDays(mRepeat);
                }
            }
        });

        mRFriTButton = (ToggleButton) v.findViewById(R.id.set_repeat_fri);
        mRFriTButton.setChecked(mRepeat.charAt(Alarm.FRI) == 'T');
        mRFriTButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRFriTButton.setChecked(mRepeat.charAt(Alarm.FRI) != 'T');

                StringBuilder sb = new StringBuilder(mRepeat);
                if (mRepeat.charAt(Alarm.FRI) == 'T') {
                    sb.setCharAt(Alarm.FRI, 'F');
                    mRepeat = sb.toString();
                    mAlarm.setRepeatDays(mRepeat);
                } else {
                    sb.setCharAt(Alarm.FRI, 'T');
                    mRepeat = sb.toString();
                    mAlarm.setRepeatDays(mRepeat);
                }
            }
        });

        mRSatTButton = (ToggleButton) v.findViewById(R.id.set_repeat_sat);
        mRSatTButton.setChecked(mRepeat.charAt(Alarm.SAT) == 'T');
        mRSatTButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRSatTButton.setChecked(mRepeat.charAt(Alarm.SAT) != 'T');

                StringBuilder sb = new StringBuilder(mRepeat);
                if (mRepeat.charAt(Alarm.SAT) == 'T') {
                    sb.setCharAt(Alarm.SAT, 'F');
                    mRepeat = sb.toString();
                    mAlarm.setRepeatDays(mRepeat);
                } else {
                    sb.setCharAt(Alarm.SAT, 'T');
                    mRepeat = sb.toString();
                    mAlarm.setRepeatDays(mRepeat);
                }
            }
        });

        mRepeatSwitch = (Switch) v.findViewById(R.id.settings_repeat_switch);
        mRepeatSwitch.setChecked(mAlarm.isRepeat());
        mRepeatSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRepeatSwitch.setChecked(!mAlarm.isRepeat());
                mAlarm.setRepeat(!mAlarm.isRepeat());
            }
        });



        mToneSpinner = (Spinner) v.findViewById(R.id.settings_tone_spinner);
        List<String> toneItems = new ArrayList<>();
        RingtoneManager ringtoneMgr = new RingtoneManager(getActivity());
        ringtoneMgr.setType(RingtoneManager.TYPE_ALARM);
        Cursor alarmsCursor = ringtoneMgr.getCursor();
        int alarmsCount = alarmsCursor.getCount();

        if (alarmsCount == 0) {
            ringtoneMgr.setType(RingtoneManager.TYPE_NOTIFICATION);
            alarmsCursor = ringtoneMgr.getCursor();
            alarmsCount = alarmsCursor.getCount();

            if (alarmsCount == 0) {
                ringtoneMgr.setType(RingtoneManager.TYPE_RINGTONE);
                alarmsCursor = ringtoneMgr.getCursor();
                alarmsCount = alarmsCursor.getCount();
            }
        }

        if (alarmsCount == 0 && !alarmsCursor.moveToFirst()) {
            Toast.makeText(getActivity(), "No sound files available", Toast.LENGTH_SHORT).show();
        }

        int previousPosition = 0;


        if (alarmsCount != 0) {
            mAlarmTones = new Uri[alarmsCount];

            String currentTone = mAlarm.getAlarmTone();

            while (!alarmsCursor.isAfterLast() && alarmsCursor.moveToNext()) {
                int currentPosition = alarmsCursor.getPosition();
                mAlarmTones[currentPosition] = ringtoneMgr.getRingtoneUri(currentPosition);
                toneItems.add(ringtoneMgr.getRingtone(currentPosition)
                        .getTitle(getActivity()));

                if (currentTone != null &&
                        currentTone.equals(mAlarmTones[currentPosition].toString())) {
                    previousPosition = currentPosition;
                }
            }

        }

        if (toneItems.isEmpty()) {
            toneItems.add("Empty");
        }


        ArrayAdapter<String> toneAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, toneItems);
        mToneSpinner.setAdapter(toneAdapter);
        mToneSpinner.setSelection(previousPosition);

        mDifficultySpinner = (Spinner) v.findViewById(R.id.settings_math_difficulty_spinner);
        String[] difficultyItems = new String[]{"Easy", "Medium", "Hard"};
        ArrayAdapter<String> difficultyAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, difficultyItems);
        mDifficultySpinner.setAdapter(difficultyAdapter);
        mDifficultySpinner.setSelection(mAlarm.getDifficulty());

        mSnoozeText = (EditText) v.findViewById(R.id.settings_snooze_text);
        mSnoozeText.setText(String.format(Locale.US, "%d",mAlarm.getSnooze()));
        mSnoozeText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    mAlarm.setSnooze(Integer.parseInt(s.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mVibrateSwitch = (Switch) v.findViewById(R.id.settings_vibrate_switch);
        mVibrateSwitch.setChecked(mAlarm.isVibrate());
        mVibrateSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVibrateSwitch.setChecked(!mAlarm.isVibrate());
                mAlarm.setVibrate(!mAlarm.isVibrate());
            }
        });

        mTestButton = (Button) v.findViewById(R.id.settings_test_button);
        mTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTestAlarm = new Alarm();
                mTestAlarm.setDifficulty(mDifficultySpinner.getSelectedItemPosition());
                if (mAlarmTones.length != 0) {
                    mTestAlarm.setAlarmTone(mAlarmTones[mToneSpinner
                            .getSelectedItemPosition()].toString());
                }
                mTestAlarm.setVibrate(mVibrateSwitch.isChecked());
                mTestAlarm.setSnooze(0);
                AlarmLab.get(getActivity()).addAlarm(mTestAlarm);
                Intent test = new Intent(getActivity(), QuestionActivity.class);
                test.putExtra(Alarm.ALARM_EXTRA, mTestAlarm.getId());
                startActivityForResult(test, REQUEST_TEST);
            }
        });

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.settings_menu, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.fragment_settings_done:

                mAlarm.setDifficulty(mDifficultySpinner.getSelectedItemPosition());
                if (mAlarmTones.length != 0) {
                    mAlarm.setAlarmTone(mAlarmTones[mToneSpinner
                            .getSelectedItemPosition()].toString());
                }

                if (mAdd) {
                    scheduleAndMessage();
                    AlarmLab.get(getActivity()).addAlarm(mAlarm);
                } else {
                    Alarm oldAlarm = AlarmLab.get(getActivity()).getAlarm(mAlarm.getId());
                    if (oldAlarm.isOn()) {
                        oldAlarm.cancelAlarm(getActivity());
                    }
                    scheduleAndMessage();
                    AlarmLab.get(getActivity()).updateAlarm(mAlarm);
                }

                getActivity().finish();
                return true;
            case R.id.fragment_settings_delete:
                if (!mAdd) {

                    if (mAlarm.isOn()) {
                        mAlarm.cancelAlarm(getActivity());
                    }
                    AlarmLab.get(getActivity()).deleteAlarm(mAlarm.getId());
                }

                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_TIME) {
            int hour = data.getIntExtra(TimePickerFragment.EXTRA_HOUR, 0);
            int min = data.getIntExtra(TimePickerFragment.EXTRA_MIN, 0);
            mAlarm.setHour(hour);
            mAlarm.setMinute(min);
            mTime.setText(mAlarm.getFormatTime());
        } else {
            if (requestCode == REQUEST_TEST) {
                AlarmLab.get(getActivity()).deleteAlarm(mTestAlarm.getId());
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putInt("hour", mAlarm.getHour());
        savedInstanceState.putInt("minute", mAlarm.getMinute());
        savedInstanceState.putString("repeat", mAlarm.getRepeat());
        savedInstanceState.putBoolean("repeatweekly", mAlarm.isRepeat());
    }

    public void scheduleAndMessage() {
        //schedule it and create a toast
        if (mAlarm.scheduleAlarm(getActivity())) {
            Toast.makeText(getActivity(), mAlarm.getTimeLeftMessage(getActivity()),
                    Toast.LENGTH_SHORT).show();
            mAlarm.setIsOn(true);
        } else {
            mAlarm.setIsOn(false);
        }
    }
}