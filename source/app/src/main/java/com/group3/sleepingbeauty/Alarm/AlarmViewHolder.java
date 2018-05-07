package com.group3.sleepingbeauty.Alarm;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.group3.sleepingbeauty.R;

public class AlarmViewHolder extends RecyclerView.ViewHolder {

    CardView cardView;
    TextView textView, txtRepeat;
    Switch switchView;
    ImageView imgVibrate, imgRepeat;
    LinearLayout alarmBar;

    public AlarmViewHolder(View itemView) {
        super(itemView);
        cardView = (CardView)itemView.findViewById(R.id.cardView);
        textView = (TextView)itemView.findViewById(R.id.alarmTextCardView);
        switchView = (Switch)itemView.findViewById(R.id.switchView);
        imgVibrate = (ImageView)itemView.findViewById(R.id.imgVibrate);
        imgRepeat = (ImageView)itemView.findViewById(R.id.imgRepeat);
        txtRepeat = (TextView)itemView.findViewById(R.id.txtRepeat);
        alarmBar = (LinearLayout)itemView.findViewById(R.id.llAlarmInfo);
    }
}
