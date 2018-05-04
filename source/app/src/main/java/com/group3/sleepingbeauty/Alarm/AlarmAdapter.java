package com.group3.sleepingbeauty.Alarm;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.group3.sleepingbeauty.R;

import org.w3c.dom.Text;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmViewHolder> {
    Context context;
    ArrayList<Alarm> alarmList;

    public AlarmAdapter(Context context, ArrayList<Alarm> list) {
        this.context = context;
        this.alarmList = list;
    }


    @Override
    public AlarmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.row_layout, parent, false);

        AlarmViewHolder alarmViewHolder = new AlarmViewHolder(itemView);
        return alarmViewHolder;
    }

    @Override
    public void onBindViewHolder(AlarmViewHolder holder, int position) {
        bindCardView(holder, alarmList.get(position));
        setCardViewOnClick(holder, position);
    }

    private void bindCardView(AlarmViewHolder holder, Alarm alarm) {
        // TODO: String getAlarmString(); and boolean isActivated();
        holder.textView.setText(alarm.getAlarmString());
        holder.switchView.setChecked(alarm.isActivated());
    }

    private void setCardViewOnClick(AlarmViewHolder holder, final int position) {
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayAlarmInfo(position);
            }
        });
    }

    private void displayAlarmInfo(int position) {
        View popUpView = LayoutInflater.from(context).inflate(R.layout.popup_box, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, R.style.Theme_Dialog);

        alertDialogBuilder.setView(popUpView);

        bindPopUpView(popUpView, position);
        setPopUpOnClick(popUpView, position);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // TODO: Save alarm info

                                // To be replaced
                                dialog.dismiss();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.dismiss();
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }

    private void setPopUpOnClick(View view, int position) {
        final CheckBox checkBox = (CheckBox)view.findViewById(R.id.checkboxPopUpView);
        LinearLayout ringtoneView = (LinearLayout)view.findViewById(R.id.buttonRingtonePopUpView);
        final TextView ringtoneTextView = (TextView)view.findViewById(R.id.textRingtonePopUpView);
        final CheckBox vibrateBox = (CheckBox)view.findViewById(R.id.checkboxVibratePopUpView);

        final TextView labelTextView = (TextView)view.findViewById(R.id.textlabelPopUpView);
        ImageView deleteButton = (ImageView)view.findViewById(R.id.imageDeletePopUpView);

        LinearLayout alarmMethodView = (LinearLayout)view.findViewById(R.id.buttonMethodPopUpView);
        final TextView alarmMethodTextView = (TextView)view.findViewById(R.id.textAlarmMethodPopUpView);

        TextView clockTextView = (TextView)view.findViewById(R.id.textClockPopUpView);

        // clock
        clockTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Time picker


            }
        });

        // repeat alarm
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Repeat alarm function
                if (checkBox.isChecked()) {
                    checkBox.setChecked(false);
                }
                else {
                    checkBox.setChecked(true);
                }
            }
        });

        // ringtone
        ringtoneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: change ringtone INTENT
                String ringtoneString = "";
                // choose new ringtone
                ringtoneTextView.setText(ringtoneString);
                // save to data

            }
        });

        // vibrate
        vibrateBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: set vibration function
                if (vibrateBox.isChecked()) {
                    vibrateBox.setChecked(false);
                }
                else {
                    vibrateBox.setChecked(true);
                }
            }
        });


        // label
        labelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: PopUP get Label
                // TODO: Save new label
                String labelString = "";

                labelTextView.setText(labelString);
            }
        });


        // delete
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: popup prompt
                AlertDialog.Builder promptBuilder = new AlertDialog.Builder(context, R.style.Theme_Dialog);
                promptBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // TODO: Delete alarm

                                        // To be replaced
                                        dialog.dismiss();
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.dismiss();
                                    }
                                })
                        .setTitle("Warning")
                        .setMessage("Do you want to delete this alarm?");

                AlertDialog promptDialog = promptBuilder.create();

                promptDialog.show();

            }
        });

        // alarm method
        alarmMethodView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: get and set alarm method - INTENT

                String alarmMethodString = "";

                alarmMethodTextView.setText(alarmMethodString);
            }
        });
    }

    private void bindPopUpView(View view, int position) {
        TextView clockTextView = (TextView)view.findViewById(R.id.textClockPopUpView);
        TextView ringtoneTextView, labelTextView;
        ringtoneTextView = (TextView)view.findViewById(R.id.textRingtonePopUpView);
        labelTextView = (TextView)view.findViewById(R.id.textlabelPopUpView);

        clockTextView.setText(alarmList.get(position).getAlarmString());
        // TODO: ringtoneLabel
        ringtoneTextView.setText(alarmList.get(position).ringtoneLabel);
        labelTextView.setText(alarmList.get(position).getLabel());
    }

    @Override
    public int getItemCount() {
        return alarmList.size();
    }
}
