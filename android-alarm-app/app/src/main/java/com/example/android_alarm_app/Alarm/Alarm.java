package com.example.android_alarm_app.Alarm;



import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.android_alarm_app.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class Alarm {

    //Declaring variables for Alarm class
    private UUID mId;
    private int alarmhour;
    private int alarmMIN;
    private boolean alarmSet;
    private String alarmSetWeekdays;
    private boolean alarmToggleON;
    private int alarmComplexity;
    private String tone;
    private boolean alarmVib;
    private int alarmStop;

    //Declaring constant variables for Alarm class
    public static final int MEDIUM = 1;
    public static final int SUN = 0;
    public static final int MON = 1;
    public static final int SAT = 6;
    public static final int WED = 3;
    public static final int THU = 4;
    public static final int FRI = 5;
    public static final int EASY = 0;
    public static final int HARD = 2;
    public static final int TUE = 2;


    //Declaring static variable for ALARM_EXTRA
    public static final String ALARM_EXTRA = "alarm_extra";

    //Alarm Method
    public Alarm() {
        mId = UUID.randomUUID();
        alarmSet = false;
        alarmSetWeekdays = "FFFFFFF";
        alarmComplexity = EASY;
        alarmStop = 5;
    }

    public void setAlarmTone(String alarmTone) {
        tone = alarmTone;
    }
    public boolean isVibrate() {
        return alarmVib;
    }
    public void setVibrate(boolean vibrate) {
        alarmVib = vibrate;
    }
    public int getSnooze() {
        return alarmStop;
    }
    public boolean isRepeat() {
        return alarmSet;
    }
    public void setRepeat(boolean repeat) {
        alarmSet = repeat;
    }
    public int getMinute() {
        return alarmMIN;
    }
    public void setMinute(int minute) {
        alarmMIN = minute;
    }
    public String getRepeat() {
        return alarmSetWeekdays;
    }
    public int getDifficulty() {
        return alarmComplexity;
    }
    public void setDifficulty(int difficulty) {
        alarmComplexity = difficulty;
    }
    public String getAlarmTone() {
        return tone;
    }
    public boolean isOn() {
        return alarmToggleON;
    }
    public void setIsOn(boolean isOn) {
        alarmToggleON = isOn;
    }
    public boolean isActive() {
        return (!alarmSetWeekdays.equals("FFFFFFF"));
    }
    public void setSnooze(int snooze) {
        alarmStop = snooze;
    }
    public Alarm(UUID id) {
        mId = id;
    }
    public UUID getId() {
        return mId;
    }
    public int getHour() {
        return alarmhour;
    }
    public void setHour(int hour) {
        alarmhour = hour;
    }
    public void setRepeatDays(String repeatDays) {
        alarmSetWeekdays = repeatDays;
    }



    //Taking the time using method
    public CharSequence getFormatTime()
    {
        //Initializing calender object
        Calendar cal = Calendar.getInstance();
        //alarm hours
        cal.set(Calendar.HOUR_OF_DAY, alarmhour);
        //alarm minutes
        cal.set(Calendar.MINUTE, alarmMIN);

        //return DateFormat
        return android.text.format.DateFormat.format("hh:mm a", cal);
    }

    //Method for settings alarm for repeating days
    public boolean scheduleAlarm(Context context)
    {
        //Intent initializing
        Intent alarm = new Intent(context, AlarmReceiver.class);
        alarm.putExtra(ALARM_EXTRA, mId);
        //Intent List
        List<PendingIntent> alarmIntent = new ArrayList<>();
        //Time List
        List<Calendar> time = new ArrayList<>();

        //Condition
        if (alarmSetWeekdays.equals("FFFFFFF")) {
            //cal object initializing
            Calendar cal = initCalendar();

            int dayOfTheWeek = getDayOfWeek(cal.get(Calendar.DAY_OF_WEEK));

            if (cal.getTimeInMillis() > System.currentTimeMillis())
            { //Current day
                StringBuilder sb = new StringBuilder("FFFFFFF");
                sb.setCharAt(dayOfTheWeek, 'T');

                alarmSetWeekdays = sb.toString();
            } else {
                //Setting it to next day
                StringBuilder sb = new StringBuilder("FFFFFFF");

                if (dayOfTheWeek == Alarm.SAT) { //if it is saturday
                    dayOfTheWeek = Alarm.SUN;
                } else {
                    dayOfTheWeek++;
                }
                sb.setCharAt(dayOfTheWeek, 'T');
                alarmSetWeekdays = sb.toString();
            }
        }

        for (int i = Alarm.SUN; i <= Alarm.SAT; i++)
        {
            //Condition
            if (alarmSetWeekdays.charAt(i) == 'T') {
                int daysUntilAlarm;
                Calendar cal = initCalendar();

                int currentDay = getDayOfWeek(cal.get(Calendar.DAY_OF_WEEK));

                if (currentDay > i ||
                        (currentDay == i && cal.getTimeInMillis() < System.currentTimeMillis())) {

                    daysUntilAlarm = Alarm.SAT - currentDay + 1 + i;

                    cal.add(Calendar.DAY_OF_YEAR, daysUntilAlarm);
                } else {
                    daysUntilAlarm = i - currentDay;

                    cal.add(Calendar.DAY_OF_YEAR, daysUntilAlarm);
                }

                StringBuilder stringId = new StringBuilder().append(i)

                        .append(alarmhour).append(alarmMIN);
                int intentId = Integer.parseInt(stringId.toString());

                //Condition to check previous alarms
                if (PendingIntent.getBroadcast(context, intentId, alarm,
                        PendingIntent.FLAG_NO_CREATE) != null) {
                    Toast.makeText(context, context.getString(R.string.alarm_duplicate_toast_text),
                            Toast.LENGTH_SHORT).show();

                    return false;
                }

                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, intentId, alarm,
                        PendingIntent.FLAG_CANCEL_CURRENT);

                alarmIntent.add(pendingIntent);
                time.add(cal);
            }
        }

        for (int i = 0; i < alarmIntent.size(); i++) {
            PendingIntent pendingIntent = alarmIntent.get(i);

            Calendar cal = time.get(i);

            AlarmManager alarmManager = (AlarmManager) context
                    .getSystemService(Context.ALARM_SERVICE);

            if (isRepeat())
            {
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),

                        AlarmManager.INTERVAL_DAY * 7, pendingIntent);
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
            }
        }

        return true;
    }

    //Snooze method implementation
    public void scheduleSnooze(Context context){

        Intent alarm = new Intent(context, AlarmReceiver.class);

        alarm.putExtra(ALARM_EXTRA, mId);

        Calendar cal = Calendar.getInstance();

        cal.add(Calendar.MINUTE, alarmStop);

        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, alarm,
                PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);

        alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), alarmIntent);

        if (alarmStop == 1) {
            Toast.makeText(context, context.getString(R.string.alarm_set_begin_msg)+" "
                            +alarmStop+context.getString(R.string.alarm_minute)+" "+
                            context.getString(R.string.alarm_set_end_msg),
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, context.getString(R.string.alarm_set_begin_msg)+" "
                            +alarmStop+context.getString(R.string.alarm_minutes)+" "+
                            context.getString(R.string.alarm_set_end_msg),
                    Toast.LENGTH_SHORT).show();
        }
    }

    //Alarm cancellation method

    public void cancelAlarm(Context context)
    {
        Intent cancel = new Intent(context, AlarmReceiver.class);

        for (int i = 0; i < 7; i++)
        {
            if (alarmSetWeekdays.charAt(i) == 'T')
            {

                StringBuilder stringId = new StringBuilder().append(i)
                        .append(alarmhour).append(alarmMIN);

                int intentId = Integer.parseInt(stringId.toString());

                PendingIntent cancelAlarmPI = PendingIntent.getBroadcast(context, intentId, cancel,
                        PendingIntent.FLAG_CANCEL_CURRENT);

                AlarmManager alarmManager = (AlarmManager)context
                        .getSystemService(Context.ALARM_SERVICE);

                alarmManager.cancel(cancelAlarmPI);

                cancelAlarmPI.cancel();
            }
        }
    }

    //Remaining time calculation method implementation
    public String getTimeLeftMessage(Context context) {
        String message;

        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.HOUR_OF_DAY, alarmhour);

        cal.set(Calendar.MINUTE, alarmMIN);

        cal.set(Calendar.SECOND, 0);

        int today = getDayOfWeek(cal.get(Calendar.DAY_OF_WEEK));

        int i, lastAlarmDay, nextAlarmDay;

        if (System.currentTimeMillis() > cal.getTimeInMillis()) {
            nextAlarmDay = today + 1;
            lastAlarmDay = today;
            if (nextAlarmDay == 7) {
                nextAlarmDay = 0;
            }

        } else {
            nextAlarmDay = today;
            lastAlarmDay = today - 1;
            if (lastAlarmDay == -1) {
                lastAlarmDay = 6;
            }
        }

        for (i = nextAlarmDay; i != lastAlarmDay; i++) {
            if(i == 7) {
                i = 0;
            }
            if (alarmSetWeekdays.charAt(i) == 'T') {
                break;
            }
        }

        if (i < today || (i == today && cal.getTimeInMillis() < System.currentTimeMillis())) {
            int daysUntilAlarm = Alarm.SAT - today + 1 + i;
            cal.add(Calendar.DAY_OF_YEAR, daysUntilAlarm);
        } else {
            int daysUntilAlarm = i - today;
            cal.add(Calendar.DAY_OF_YEAR, daysUntilAlarm);
        }

        long alarmTime = cal.getTimeInMillis();
        long remainderTime = alarmTime - System.currentTimeMillis();

        int minutes = (int) ((remainderTime / (1000*60)) % 60);
        int hours   = (int) ((remainderTime / (1000*60*60)) % 24);
        int days = (int) (remainderTime / (1000*60*60*24));

        String mString, hString, dString;

        if (minutes == 1) {
            mString = context.getString(R.string.alarm_minute);
        } else {
            mString = context.getString(R.string.alarm_minutes);
        }

        if (hours == 1) {
            hString = context.getString(R.string.alarm_hour);
        } else {
            hString = context.getString(R.string.alarm_hours);
        }

        if (days == 1) {
            dString = context.getString(R.string.alarm_day);
        } else {
            dString = context.getString(R.string.alarm_days);
        }

        if (days == 0) {
            if (hours == 0) {
                message = context.getString(R.string.alarm_set_begin_msg)+" "+minutes+" "
                        +mString+" "+context.getString(R.string.alarm_set_end_msg);
            } else {
                message = context.getString(R.string.alarm_set_begin_msg)+" "
                        +hours+" "+hString+" "+minutes+" "+mString+" "+
                        context.getString(R.string.alarm_set_end_msg);
            }
        } else {
            message = context.getString(R.string.alarm_set_begin_msg)+" "
                    +days+" "+dString+" "+hours+" "+hString+" "+minutes+" "+
                    mString+" "+context.getString(R.string.alarm_set_end_msg);
        }

        return message;
    }

    private Calendar initCalendar() {
        Calendar cal = Calendar.getInstance();
        //Hours
        cal.set(Calendar.HOUR_OF_DAY, alarmhour);
        //Minutes
        cal.set(Calendar.MINUTE, alarmMIN);
        //Seconds
        cal.set(Calendar.SECOND, 0);
        //Return
        return cal;
    }

    //Method for getting the day
    public int getDayOfWeek(int day) {
        int dayOfWeek;
        int errorValue = 8;

        switch(day)
        {
            case Calendar.SUNDAY:
                dayOfWeek = Alarm.SUN;
                break;
            case Calendar.MONDAY:
                dayOfWeek = Alarm.MON;
                break;
            case Calendar.TUESDAY:
                dayOfWeek = Alarm.TUE;
                break;
            case Calendar.WEDNESDAY:
                dayOfWeek = Alarm.WED;
                break;
            case Calendar.THURSDAY:
                dayOfWeek = Alarm.THU;
                break;
            case Calendar.FRIDAY:
                dayOfWeek = Alarm.FRI;
                break;
            case Calendar.SATURDAY:
                dayOfWeek = Alarm.SAT;
                break;
            default:
                return errorValue;
        }
    //return the day
        return dayOfWeek;
    }
}
