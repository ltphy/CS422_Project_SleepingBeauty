package com.group3.sleepingbeauty.Alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

import com.group3.sleepingbeauty.AlarmActivity;
import com.group3.sleepingbeauty.Utils.Global;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Lam Le Thanh The on 4/5/2018.
 */

public class Alarm {
    private Context context;
    private Calendar calendar;
    private String offMethod;
    private long repeatInterval; // in day
    private Uri sound;
    private int volume;
    private boolean vibrate;
    private boolean active;

    private AlarmManager alarmManager = null;
    private PendingIntent alarmIntent = null;

    public Alarm(Context context, Calendar calendar, String offMethod, long repeatInterval, Uri sound, int volume, boolean vibrate, boolean active) {
        this.context = context;
        this.calendar = calendar;
        this.offMethod = offMethod;
        this.repeatInterval = repeatInterval;
        this.sound = sound;
        this.volume = volume;
        this.vibrate = vibrate;
        this.active = active;

        normalize();
    }

    private void normalize() {
        if (volume > 100)
            volume = 100;
        else if (volume < 0)
            volume = 0;

        if (repeatInterval < 0)
            repeatInterval = 0;
    }

    public void fire() {
        if (active) {
            alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, AlarmActivity.class);

            //int h = calendar.get(Calendar.HOUR_OF_DAY), m = calendar.get(Calendar.MINUTE);
            //String code = String.valueOf(h) + ' ' + String.valueOf(m);
            String code = "Alarm"; // demo only, should use different code for different alarm

            intent.putExtra(code, String.valueOf(active) + ',' + String.valueOf(vibrate) + ',' + String.valueOf(volume) + ',' + (sound == null ? "null" : sound.toString()));
            alarmIntent = PendingIntent.getActivity(context, 0, intent, intent.FILL_IN_DATA | PendingIntent.FLAG_UPDATE_CURRENT);

            /*
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(System.currentTimeMillis());
            c.set(Calendar.HOUR_OF_DAY, h);
            c.set(Calendar.MINUTE, m);
            c.set(Calendar.SECOND, 0);
            */

            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, 5 * 1000, alarmIntent);
            // fire in 5 s, for demo only, should use RTC_WAKEUP and the exact time set
        }
    }

    public void cancel() { // must be fired first somewhere
        if (!active && alarmIntent != null && alarmManager != null) {
            alarmManager.cancel(alarmIntent);
        }
    }

    public void toggleOnOff() {
        active = !active;
        if (active)
            fire();
        else
            cancel();
    }

    public boolean isActive() {
        return active;
    }

    public boolean isVibrate() {
        return vibrate;
    }

    public void setVibrate(boolean vibrate) {
        this.vibrate = vibrate;
    }

    public Long getRepeatInterval() {
        return repeatInterval;
    }

    public void setRepeatInterval(long repeatInterval) {
        this.repeatInterval = repeatInterval;
    }

    public void setSound(Uri sound) {
        this.sound = sound;
    }

    public String getSound() {
        Ringtone ringtone = RingtoneManager.getRingtone(context, sound);
        return ringtone.getTitle(context);
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public String getAlarmString() {
        if (calendar == null)
            return "New alarm";
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a", Locale.US);
        return formatter.format(calendar.getTime());
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public Alarm copy() {
        Calendar c = Calendar.getInstance();
        if (calendar != null)
            c.setTime(calendar.getTime());
        return new Alarm(context, c, offMethod, repeatInterval, sound, volume, vibrate, active);
    }

    public void paste(Alarm src) {
        context = src.context;
        calendar = src.calendar;
        offMethod = src.offMethod;
        repeatInterval = src.repeatInterval;
        sound = src.sound;
        volume = src.volume;
        vibrate = src.vibrate;
        active = src.active;
    }
}
