package com.group3.sleepingbeauty;

import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.group3.sleepingbeauty.Alarm.Alarm;
import com.group3.sleepingbeauty.Alarm.AlarmAdapter;
import com.group3.sleepingbeauty.Utils.Global;
import com.group3.sleepingbeauty.Utils.MovableFloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Alarm> alarmList;
    private AlarmAdapter alarmAdapter;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_alarm:
                    initAlarmView();
                    return true;
                case R.id.navigation_clock:
                    initClockView();
                    return true;
                case R.id.navigation_notifications:
                    Global.notImplementedPrompt(MainActivity.this);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Must retrieve the list of fired (and not repeated) alarms to turn them off

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_clock);

        alarmList = new ArrayList<>();
        alarmList.add(new Alarm(MainActivity.this, Calendar.getInstance(), "quiz", 0, null, 100, true, false));
        alarmList.add(new Alarm(MainActivity.this, Calendar.getInstance(), "quiz", 2, null, 50, true, false));
        alarmList.add(new Alarm(MainActivity.this, Calendar.getInstance(), "quiz", 0, null, 80, false, false));


        alarmAdapter = new AlarmAdapter(MainActivity.this, alarmList);

        alarmAdapter.notifyDataSetChanged();

        ((TextView)findViewById(R.id.textDateMainView)).setText(new SimpleDateFormat("EEE, MMM d, yyyy", Locale.US).format(Calendar.getInstance().getTime()));
    }

    private void initAlarmView() {
        // hide clock
        LinearLayout clockMainView = (LinearLayout)findViewById(R.id.clockMainView);
        clockMainView.setVisibility(View.GONE);

        final RecyclerView rv = (RecyclerView)findViewById(R.id.recyclerView);
        rv.setVisibility(View.VISIBLE);

        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);

        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(alarmAdapter);

        // show FAB
        MovableFloatingActionButton fab = (MovableFloatingActionButton) findViewById(R.id.floatingActionButtonView);
        fab.setVisibility(View.VISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int n = alarmList.size();
                alarmList.add(new Alarm(MainActivity.this, null, "quiz", 0, null, 100, false, false));
                alarmAdapter.notifyItemInserted(n);
                layoutManager.smoothScrollToPosition(rv, null, n);
            }
        });
    }

    private void initClockView() {
        // hide FAB
        MovableFloatingActionButton fab = (MovableFloatingActionButton) findViewById(R.id.floatingActionButtonView);
        fab.setVisibility(View.GONE);

        // show clock
        LinearLayout clockMainView = (LinearLayout)findViewById(R.id.clockMainView);
        clockMainView.setVisibility(View.VISIBLE);

        RecyclerView rv = (RecyclerView)findViewById(R.id.recyclerView);
        rv.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == Global.REQ_CODE_RINGTONE_SYS) {
                Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                alarmAdapter.updateRingtone(uri);
            }
        }
    }
}
