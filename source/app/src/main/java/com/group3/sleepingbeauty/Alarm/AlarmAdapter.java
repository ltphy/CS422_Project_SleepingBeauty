package com.group3.sleepingbeauty.Alarm;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.group3.sleepingbeauty.MainActivity;
import com.group3.sleepingbeauty.R;
import com.group3.sleepingbeauty.Utils.Global;

import java.util.ArrayList;
import java.util.Calendar;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmViewHolder> {
    private Context context;
    private ArrayList<Alarm> alarmList;
    private Alarm dummy;
    private AlertDialog alertDialog, promptDialog;
    private View popUpView;

    public AlarmAdapter(Context context, ArrayList<Alarm> list) {
        this.context = context;
        this.alarmList = list;
    }

    @Override
    public AlarmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.row_layout, parent, false);
        return new AlarmViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AlarmViewHolder holder, int position) {
        bindCardView(holder, alarmList.get(position));
        setCardViewOnClick(holder, position);
    }

    private void bindCardView(AlarmViewHolder holder, final Alarm alarm) {
        holder.textView.setText(alarm.getAlarmString());
        holder.switchView.setChecked(alarm.isActive());

        holder.switchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                alarm.toggleOnOff();
            }
        });

        holder.alarmBar.setVisibility(View.GONE);
        if (alarm.isVibrate()) {
            holder.alarmBar.setVisibility(View.VISIBLE);
            holder.imgVibrate.setVisibility(View.VISIBLE);
        }
        else
            holder.imgVibrate.setVisibility(View.GONE);

        Long d = alarm.getRepeatInterval();
        if (d > 0) {
            holder.alarmBar.setVisibility(View.VISIBLE);
            holder.imgRepeat.setVisibility(View.VISIBLE);
            holder.txtRepeat.setText(d.toString() + ' ' + context.getResources().getString(d > 1 ? R.string.days : R.string.day));
        } else {
            holder.imgRepeat.setVisibility(View.GONE);
            holder.txtRepeat.setText("");
        }
    }

    private void setCardViewOnClick(AlarmViewHolder holder, final int position) {
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dummy = alarmList.get(position).copy();
                displayAlarmInfo(position);
            }
        });
    }

    private void displayAlarmInfo(final int position) {
        popUpView = LayoutInflater.from(context).inflate(R.layout.popup_box, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, R.style.Theme_AppCompat_Dialog_Alert);

        alertDialogBuilder.setView(popUpView);

        initPopUp(popUpView, position);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                Alarm cur = alarmList.get(position);
                                cur.cancel();
                                cur.paste(dummy);
                                cur.fire();
                                dialog.dismiss();

                                notifyDataSetChanged();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.dismiss();
                            }
                        });

        alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }

    private void initPopUp(View view, final int position) {
        final CheckBox repeatBox = (CheckBox)view.findViewById(R.id.checkboxRepeat);
        LinearLayout ringtoneView = (LinearLayout)view.findViewById(R.id.buttonRingtonePopUpView);
        final TextView ringtoneTextView = (TextView)view.findViewById(R.id.textRingtonePopUpView);
        final CheckBox vibrateBox = (CheckBox)view.findViewById(R.id.checkboxVibratePopUpView);
        final TextView clockTextView = (TextView)view.findViewById(R.id.textClockPopUpView);
        ImageView deleteButton = (ImageView)view.findViewById(R.id.imageDeletePopUpView);
        LinearLayout alarmMethodView = (LinearLayout)view.findViewById(R.id.buttonMethodPopUpView);
        final TextView alarmMethodTextView = (TextView)view.findViewById(R.id.textAlarmMethodPopUpView);
        final LinearLayout repeatLayout = (LinearLayout)view.findViewById(R.id.llRepeat);
        final EditText edtDay = (EditText)view.findViewById(R.id.edtDay);
        final TextView txtDay = (TextView)view.findViewById(R.id.txtDay);
        final SeekBar seekVol = (SeekBar)view.findViewById(R.id.seekVol);

        final Calendar c = dummy.getCalendar();
        final TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                c.set(Calendar.HOUR_OF_DAY, i);
                c.set(Calendar.MINUTE, i1);
                clockTextView.setText(dummy.getAlarmString());
            }
        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false);

        clockTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialog.show();
            }
        });
        clockTextView.setText(dummy.getAlarmString());

        edtDay.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String s = editable.toString();
                if (s.isEmpty()) {
                    txtDay.setText(R.string.day);
                    dummy.setRepeatInterval(1);
                } else {
                    Long d = Long.parseLong(s);
                    if (d > 1)
                        txtDay.setText(R.string.days);
                    else
                        txtDay.setText(R.string.day);
                    dummy.setRepeatInterval(d);
                }
            }
        });

        repeatBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    repeatLayout.setVisibility(View.VISIBLE);
                    Long d = dummy.getRepeatInterval();
                    if (d > 0)
                        edtDay.setText(d.toString());
                    else
                        edtDay.setText("");
                }
                else {
                    repeatLayout.setVisibility(View.GONE);
                    dummy.setRepeatInterval(0);
                }
            }
        });
        Long d = dummy.getRepeatInterval();
        repeatBox.setChecked(d > 0);
        if (repeatBox.isChecked()) {
            repeatLayout.setVisibility(View.VISIBLE);
            edtDay.setText(d.toString());
        }
        else
            repeatLayout.setVisibility(View.GONE);

        vibrateBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                dummy.setVibrate(b);
            }
        });
        vibrateBox.setChecked(dummy.isVibrate());

        ringtoneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: change ringtone INTENT

            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: popup prompt
                AlertDialog.Builder promptBuilder = new AlertDialog.Builder(context, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                promptBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        alarmList.remove(position);
                                        dialog.dismiss();
                                        alertDialog.dismiss();

                                        notifyItemRemoved(position);
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                })
                        .setTitle("Warning")
                        .setMessage("Do you want to delete this alarm?");

                promptDialog = promptBuilder.create();
                promptDialog.show();
            }
        });

        alarmMethodView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: get and set alarm method - INTENT

            }
        });

        ringtoneTextView.setText(dummy.getSound());
        ringtoneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder ringtonePickerBuilder = new AlertDialog.Builder(context, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                ringtonePickerBuilder.setCancelable(true)
                        .setTitle("Ringtone Picking Options")
                        .setMessage("Which type of ringtones would you like to find?")
                        .setPositiveButton("System", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select a ringtone:");
                                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, true);
                                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, false);
                                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
                                dialogInterface.dismiss();
                                ((MainActivity)context).startActivityForResult(intent, Global.REQ_CODE_RINGTONE_SYS);
                            }
                        })
                        .setNegativeButton("External", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Global.notImplementedPrompt(context);
                                dialogInterface.dismiss();
                            }
                        });

                AlertDialog ringtonePicker = ringtonePickerBuilder.create();
                ringtonePicker.show();
            }
        });

        seekVol.setProgress(dummy.getVolume());
        seekVol.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                dummy.setVolume(seekBar.getProgress());
            }
        });
    }

    public void updateRingtone(Uri uri) {
        dummy.setSound(uri);
        ((TextView)popUpView.findViewById(R.id.textRingtonePopUpView)).setText(dummy.getSound());
    }

    @Override
    public int getItemCount() {
        return alarmList.size();
    }
}
