package com.group3.sleepingbeauty;

import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Random;

// ASSUME one alarm to handle at a time

public class AlarmActivity extends AppCompatActivity {
    LinearLayout ll;
    TextView txtQues;
    EditText edtAns;

    int count = 0;
    static final int MAX = 1;
    int res;

    Ringtone ringtone;
    Vibrator vibrator;
    long[] vibPattern = {0, 100, 1000};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        startAlarm();

        initViews();
        res = generateQuestion();
    }

    private void startAlarm() {
        Intent intent = getIntent();
        //Calendar c = Calendar.getInstance();
        //String code = String.valueOf(c.get(Calendar.HOUR_OF_DAY)) + ' ' + String.valueOf(c.get(Calendar.MINUTE));
        String code = "Alarm";
        String raw = intent.getStringExtra(code);
        String[] dat = raw.split(",");

        boolean on = Boolean.parseBoolean(dat[0]), vib = Boolean.parseBoolean(dat[1]);

        if (on) {
            /*
            // Default ringtone demo:
            Uri currentRingtoneUri = Uri.parse("android.resource://com.group3.sleepingbeauty/" + R.raw.sample_ringtone);
            Ringtone currentRingtone = RingtoneManager.getRingtone(context, currentRingtoneUri);

            AudioAttributes aa = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();
            currentRingtone.setAudioAttributes(aa);
            currentRingtone.play();
            */

            int volume = Integer.parseInt(dat[2]);
            AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            audioManager.setStreamVolume(AudioManager.STREAM_ALARM, audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM) * volume / 100, 0);

            Uri ringtoneUri;
            if (dat[3].equals("null")) {
                ringtoneUri = Uri.parse("android.resource://com.group3.sleepingbeauty/" + R.raw.sample_ringtone);
                ringtone = RingtoneManager.getRingtone(this, ringtoneUri);

                AudioAttributes aa = new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build();
                ringtone.setAudioAttributes(aa);
            } else {
                ringtoneUri = Uri.parse(dat[3]);
                ringtone = RingtoneManager.getRingtone(this, ringtoneUri);
            }
            ringtone.play();

            vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(vibPattern, 0);
        }
    }

    private int generateQuestion() {
        int res, sign, adder;
        String ques;

        Random r = new Random();
        adder = r.nextInt(100);

        ques = String.valueOf(adder);
        res = adder;

        for (int i = 0; i < 2; ++i) {
            sign = r.nextInt(2);
            adder = r.nextInt(100);
            if (sign == 0) {
                ques += " - ";
                res -= adder;
            } else {
                ques += " + ";
                res += adder;
            }
            ques += String.valueOf(adder);
        }
        ques += "\n=";

        txtQues.setText(ques);
        return res;
    }

    private void initViews() {
        ll = findViewById(R.id.llAlarm);
        txtQues = findViewById(R.id.txtQues);
        edtAns = findViewById(R.id.edtAns);
    }

    public void snooze(View view) {
        Snackbar.make(ll, "You are too evil to deserve snoozing. Please keep up with the quiz!", Snackbar.LENGTH_LONG).show();
    }

    public void off(View view) {
        String s = edtAns.getText().toString();

        if (s.isEmpty())
            Snackbar.make(ll, "Quit even before trying to solve the quiz? Not as you wish!", Snackbar.LENGTH_LONG).show();
        else if (res == Integer.parseInt(s)) {
            ++count;
            if (count < MAX) {
                Snackbar.make(ll, "Life is not that easy! Try some more!", Snackbar.LENGTH_LONG).show();
                edtAns.setText("");
                res = generateQuestion();
            } else {
                ringtone.stop();
                vibrator.cancel();
                finish();
            }
        } else
            Snackbar.make(ll, "Not the correct answer, but we think you can make it...", Snackbar.LENGTH_LONG).show();
    }
}
