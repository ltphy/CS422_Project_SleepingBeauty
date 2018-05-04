package com.group3.sleepingbeauty;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.os.Bundle;
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
import android.widget.TimePicker;

import com.group3.sleepingbeauty.Alarm.AlarmAdapter;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private ArrayList<Alarm> alarmList;
    private AlarmAdapter alarmAdapter;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_alarm:
                    mTextMessage.setText(R.string.title_home);
                    initAlarmView();
                    return true;
                case R.id.navigation_clock:
                    mTextMessage.setText(R.string.title_dashboard);
                    initClockView();
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_clock);

        alarmAdapter = new AlarmAdapter(MainActivity.this, alarmList);

        // TODO: Retrieve alarm data to alarmList

        alarmAdapter.notifyDataSetChanged();
    }

    private void initAlarmView() {

        // show FAB
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.floatingActionButtonView);
        fab.setVisibility(View.VISIBLE);

        // hide clock
        LinearLayout clockMainView = (LinearLayout)findViewById(R.id.clockMainView);
        clockMainView.setVisibility(View.INVISIBLE);

        RecyclerView rv = (RecyclerView)findViewById(R.id.recyclerView);
        rv.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);

        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(alarmAdapter);
    }

    private void initClockView() {
        // hide FAB
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.floatingActionButtonView);
        fab.setVisibility(View.INVISIBLE);

        // show clock
        LinearLayout clockMainView = (LinearLayout)findViewById(R.id.clockMainView);
        clockMainView.setVisibility(View.VISIBLE);

        RecyclerView rv = (RecyclerView)findViewById(R.id.recyclerView);
        rv.setVisibility(View.INVISIBLE);


    }

}
