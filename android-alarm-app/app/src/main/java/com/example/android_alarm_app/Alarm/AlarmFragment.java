package com.example.android_alarm_app.Alarm;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android_alarm_app.AlarmOptions;
import com.example.android_alarm_app.R;

import java.util.List;

public class AlarmFragment extends Fragment {

    private RecyclerView mAlarmRecyclerView;
    private RelativeLayout mEmptyView;
    private AlarmAdapter mAdapter;

    public static final String GET_ALARM = "GET";

    public static AlarmFragment newInstance() {
        return new AlarmFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_alarm_list, container, false);

        mAlarmRecyclerView = (RecyclerView) v.findViewById(R.id.alarm_recycler_view);

        mAlarmRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mEmptyView = (RelativeLayout) v.findViewById(R.id.alarm_empty_view);

        updateUI();

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.fragment_add_alarm_menu:
                Intent intent = new Intent(getContext(), AlarmOptions.class);

                intent.putExtra(GET_ALARM, "");
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        updateUI();
    }

    //Update the interface of the activity
    public void updateUI() {
        AlarmLab alarmLab = AlarmLab.get(getActivity());
        List<Alarm> alarms = alarmLab.getAlarms();

        if (mAdapter == null) {
            mAdapter = new AlarmAdapter(alarms);
            mAlarmRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setAlarms(alarms);
            mAdapter.notifyDataSetChanged();
        }

        if (mAdapter.getItemCount() == 0) {
            mEmptyView.setVisibility(View.VISIBLE);
        } else {
            mEmptyView.setVisibility(View.GONE);
        }
    }


        private class AlarmHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Alarm mAlarm;
        private String mRepeat;
        private TextView mTimeTextView;
        private TextView mRepeatTextViewMon;
        private TextView mRepeatTextViewTue;
        private TextView mRepeatTextViewWed;
        private TextView mRepeatTextViewThu;
        private TextView mRepeatTextViewFri;
        private TextView mRepeatTextViewSat;
        private TextView mRepeatTextViewSun;
        private Switch mSwitchButton;
        private RelativeLayout mLayout;

        public AlarmHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mLayout = (RelativeLayout) itemView.findViewById(R.id.list_item);
            mTimeTextView = (TextView) itemView.findViewById(R.id.alarm_time);
            mRepeatTextViewMon = (TextView) itemView.findViewById(R.id.alarm_repeat_mon);
            mRepeatTextViewTue = (TextView) itemView.findViewById(R.id.alarm_repeat_tue);
            mRepeatTextViewWed = (TextView) itemView.findViewById(R.id.alarm_repeat_wed);
            mRepeatTextViewThu = (TextView) itemView.findViewById(R.id.alarm_repeat_thu);
            mRepeatTextViewFri = (TextView) itemView.findViewById(R.id.alarm_repeat_fri);
            mRepeatTextViewSat = (TextView) itemView.findViewById(R.id.alarm_repeat_sat);
            mRepeatTextViewSun = (TextView) itemView.findViewById(R.id.alarm_repeat_sun);

            mSwitchButton = (Switch) itemView.findViewById(R.id.alarm_switch_button);
            mSwitchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAlarm.setIsOn(!mAlarm.isOn());
                    mSwitchButton.setChecked(mAlarm.isOn());

                    if (mAlarm.isOn()) {
                        if (mAlarm.scheduleAlarm(getActivity())) {
                            Toast.makeText(getActivity(), mAlarm.getTimeLeftMessage(getActivity()),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            mAlarm.setIsOn(false);
                            mSwitchButton.setChecked(false);
                        }
                    } else {
                        mAlarm.cancelAlarm(getActivity());
                    }

                    AlarmLab.get(getActivity()).updateAlarm(mAlarm);
                }
            });
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(), AlarmOptions.class);
            intent.putExtra(GET_ALARM, mAlarm.getId().toString());
            startActivity(intent);
        }

        public void bindAlarm(Alarm alarm) {

            mAlarm = alarm;
            mRepeat = alarm.getRepeat();

            int hour = mAlarm.getHour();
            if (hour < 12) {
                mLayout.setBackgroundColor(ContextCompat
                        .getColor(getContext(), R.color.colorNightBlue));
            } else {
                mLayout.setBackgroundColor(ContextCompat
                        .getColor(getContext(), R.color.colorNightBlue));
            }

            mSwitchButton.setChecked(mAlarm.isOn());

            mTimeTextView.setText(mAlarm.getFormatTime());
            int color;
            if (mAlarm.isRepeat()) {
                color = R.color.colorGold;
            } else {
                color = R.color.colorWhite;
            }
            mTimeTextView.setTextColor(ContextCompat.getColor(getContext(),
                    color));

            if (mRepeat.charAt(Alarm.SUN) == 'T') {
                mRepeatTextViewSun.setTextColor(ContextCompat.getColor(getContext(),
                        R.color.colorWhite));
            } else
                mRepeatTextViewSun.setTextColor(ContextCompat.getColor(getContext(),
                        R.color.colorWhite));

            if (mRepeat.charAt(Alarm.MON) == 'T') {
                mRepeatTextViewMon.setTextColor(ContextCompat.getColor(getContext(),
                        R.color.colorNightBlue));
            } else
                mRepeatTextViewMon.setTextColor(ContextCompat.getColor(getContext(),
                        R.color.colorNightBlue));

            if (mRepeat.charAt(Alarm.TUE) == 'T') {
                mRepeatTextViewTue.setTextColor(ContextCompat.getColor(getContext(),
                        R.color.colorNightBlue));
            } else
                mRepeatTextViewTue.setTextColor(ContextCompat.getColor(getContext(),
                        R.color.colorNightBlue));

            if (mRepeat.charAt(Alarm.WED) == 'T') {
                mRepeatTextViewWed.setTextColor(ContextCompat.getColor(getContext(),
                        R.color.colorNightBlue));
            } else
                mRepeatTextViewWed.setTextColor(ContextCompat.getColor(getContext(),
                        R.color.colorNightBlue));

            if (mRepeat.charAt(Alarm.THU) == 'T') {
                mRepeatTextViewThu.setTextColor(ContextCompat.getColor(getContext(),
                        R.color.colorNightBlue));
            } else
                mRepeatTextViewThu.setTextColor(ContextCompat.getColor(getContext(),
                        R.color.colorNightBlue));

            if (mRepeat.charAt(Alarm.FRI) == 'T') {
                mRepeatTextViewFri.setTextColor(ContextCompat.getColor(getContext(),
                        R.color.colorNightBlue));
            } else
                mRepeatTextViewFri.setTextColor(ContextCompat.getColor(getContext(),
                        R.color.colorNightBlue));

            if (mRepeat.charAt(Alarm.SAT) == 'T') {
                mRepeatTextViewSat.setTextColor(ContextCompat.getColor(getContext(),
                        R.color.colorNightBlue));
            } else {
                mRepeatTextViewSat.setTextColor(ContextCompat.getColor(getContext(),
                        R.color.colorNightBlue));
            }


        }
    }

    private class AlarmAdapter extends RecyclerView.Adapter<AlarmHolder> {

        private List<Alarm> mAlarms;

        public AlarmAdapter(List<Alarm> alarms) {
            mAlarms = alarms;
        }

        @Override
        public AlarmHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            View view = layoutInflater.inflate(R.layout.activity_item, parent, false);

            return new AlarmHolder(view);
        }

        @Override
        public void onBindViewHolder(AlarmHolder holder, int position)
        {
            Alarm alarm = mAlarms.get(position);

            holder.bindAlarm(alarm);

        }

        @Override
        public int getItemCount() {
            return mAlarms.size();
        }

        public void setAlarms(List<Alarm> alarms) {
            mAlarms = alarms;
        }
    }
}
