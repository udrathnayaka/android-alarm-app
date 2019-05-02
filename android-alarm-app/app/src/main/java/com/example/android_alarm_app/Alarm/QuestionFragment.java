package com.example.android_alarm_app.Alarm;

import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android_alarm_app.R;

import java.io.IOException;
import java.util.Calendar;
import java.util.Random;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.UUID;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;



public class QuestionFragment extends Fragment {

    TextView mQuestion;
    TextView mAnswer;
    Button onebtn;
    Button twobtn;
    Button threebtn;
    Button fourbtn;
    Button fivebtn;
    Button sixbtn;
    Button sevenbtn;
    Button eightbtn;
    Button ninebtn;
    Button zerobtn;
    Button setbtn;
    Button delbtn;
    Button clrbtn;
    Button snzbtn;

    private StringBuilder sb;
    private int op, num1, num2, ans;

    private static final int ADD = 0;
    private static final int SUBTRACT = 1;
    private static final int TIMES = 2;
    private static final int DIVIDE = 3;

    private MediaPlayer mp = new MediaPlayer();
    private boolean vibrateRunning = false;

    public static QuestionFragment newInstance() {
        return new QuestionFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {

        Intent intent = getActivity().getIntent();
        Bundle extra = intent.getExtras();

        UUID alarmId = (UUID) extra.get(Alarm.ALARM_EXTRA);
        final Alarm alarm = AlarmLab.get(getActivity()).getAlarm(alarmId);

        View v = inflater.inflate(R.layout.activity_question, parent, false);

        int dayOfTheWeek = alarm.getDayOfWeek(Calendar.getInstance()
                .get(Calendar.DAY_OF_WEEK));

        if (!alarm.isRepeat()) {
            StringBuilder repeat = new StringBuilder(alarm.getRepeat());
            repeat.setCharAt(dayOfTheWeek, 'F');
            alarm.setRepeatDays(repeat.toString());
            if (!alarm.isActive()) {
                alarm.setIsOn(false);
            }
            AlarmLab.get(getActivity()).updateAlarm(alarm);
        }


        //Play alarm tone
        if (!alarm.getAlarmTone().isEmpty()) {
            Uri alarmUri = Uri.parse(alarm.getAlarmTone());

            try {
                mp.setDataSource(getContext(), alarmUri);
                mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mp.prepare();
                mp.setLooping(true);
                mp.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getActivity(), getString(R.string.tone_not_available),
                    Toast.LENGTH_SHORT).show();
        }

        //Vibrate phone
        if (alarm.isVibrate()) {
            vibrateRunning = true;
            Thread thread = new Thread(new Runnable(){
                @Override
                public void run(){
                    while(vibrateRunning) {

                        Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                        v.vibrate(1000);

                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                        }
                    }

                    if (!vibrateRunning) {
                        return;
                    }
                }
            });
            thread.start();

        }

        //Complexity
        getMathProblem(alarm.getDifficulty());

        //setting onClick listeners
        sb = new StringBuilder("");

        mQuestion = (TextView) v.findViewById(R.id.problem);

        mQuestion.setText(getMathString());

        mAnswer = (TextView) v.findViewById(R.id.solution);

        onebtn = (Button) v.findViewById(R.id.btn01);

        delbtn = (Button) v.findViewById(R.id.deletebutton);
        delbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sb.length() != 0) {
                    sb.deleteCharAt(sb.length() - 1);
                    mAnswer.setText(sb);
                }
            }
        });
        clrbtn = (Button) v.findViewById(R.id.clearbutton);


        clrbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sb.delete(0, sb.length());
                mAnswer.setText(sb);
            }
        });
        setbtn = (Button) v.findViewById(R.id.cancelbutton);


        setbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(sb.toString()) != ans) {
                    Toast.makeText(getActivity(), "Incorrect!", Toast.LENGTH_SHORT).show();
                    sb.setLength(0);
                    mAnswer.setText("");
                } else {
                    mp.stop();
                    vibrateRunning = false;
                    getActivity().setResult(Activity.RESULT_OK);
                    getActivity().finish();
                }
            }
        });

        onebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sb.append(1);
                mAnswer.setText(sb);
            }
        });
        twobtn = (Button) v.findViewById(R.id.btn2);


        twobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sb.append(2);
                mAnswer.setText(sb);
            }
        });
        threebtn = (Button) v.findViewById(R.id.btn3);


        threebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sb.append(3);
                mAnswer.setText(sb);
            }
        });
        fourbtn = (Button) v.findViewById(R.id.btn4);


        fourbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sb.append(4);
                mAnswer.setText(sb);
            }
        });
        fivebtn = (Button) v.findViewById(R.id.btn5);
        fivebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sb.append(5);
                mAnswer.setText(sb);
            }
        });
        sixbtn = (Button) v.findViewById(R.id.btn6);


        sixbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sb.append(6);
                mAnswer.setText(sb);
            }
        });
        sevenbtn = (Button) v.findViewById(R.id.btn7);


        sevenbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sb.append(7);
                mAnswer.setText(sb);
            }
        });


        eightbtn = (Button) v.findViewById(R.id.btn8);
        eightbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sb.append(8);
                mAnswer.setText(sb);
            }
        });
        ninebtn = (Button) v.findViewById(R.id.btn9);


        ninebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sb.append(9);
                mAnswer.setText(sb);
            }
        });
        zerobtn = (Button) v.findViewById(R.id.btn0);


        zerobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sb.append(0);
                mAnswer.setText(sb);
            }
        });

        snzbtn = (Button) v.findViewById(R.id.snzbutton);
        snzbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alarm.getSnooze() == 0) {
                    Toast.makeText(getActivity(),
                            getString(R.string.snooze_off), Toast.LENGTH_SHORT).show();
                } else {
                    mp.stop();
                    vibrateRunning = false;
                    alarm.scheduleSnooze(getActivity());
                    getActivity().finish();
                }
            }
        });

        return v;
    }

  //Complexity
    private void getMathProblem(int difficulty) {
        Random random = new Random();

        op = random.nextInt(4);
        int add1, add2, mult1, mult2;

        switch(difficulty) {
            case Alarm.EASY:
                add1 = 90;
                add2 = 10;
                mult1 = 10;
                mult2 = 3;
                break;
            case Alarm.HARD:
                add1 = 9000;
                add2 = 1000;
                mult1 = 14;
                mult2 = 12;
                break;
            default:
                add1 = 900;
                add2 = 100;
                mult1 = 13;
                mult2 = 3;
                break;
        }

        switch (op) {
            case ADD:
                num1 = random.nextInt(add1) + add2;
                num2 = random.nextInt(add1) + add2;
                ans = num1 + num2;
                break;
            case SUBTRACT:
                num1 = random.nextInt(add1) + add2;
                num2 = random.nextInt(add1) + add2;

                if (num1 < num2) {
                    int temp = num1;
                    num1 = num2;
                    num2 = temp;
                }
                ans = num1 - num2;
                break;
            case TIMES:
                num1 = random.nextInt(mult1) + mult2;
                num2 = random.nextInt(mult1) + mult2;
                ans = num1 * num2;
                break;
            case DIVIDE:
                num1 = random.nextInt(mult1) + mult2;
                num2 = random.nextInt(mult1) + mult2;
                ans = num1 * num2;

                int tmp = ans;
                ans = num1;
                num1 = tmp;
                break;
            default:
                break;
        }
    }

    //Question method
    private String getMathString() {
        switch(op) {
            case ADD:
                return (num1 + " + " + num2 + " = ");
            case SUBTRACT:
                return (num1 + " - " + num2 + " = ");
            case TIMES:
                return (num1 + " x " + num2 + " = ");
            case DIVIDE:
                return (num1 + " / " + num2 + " = ");
            default:
                return null;
        }
    }

}
