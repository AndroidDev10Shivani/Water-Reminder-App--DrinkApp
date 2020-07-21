package com.sample.drinkup;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class SettingFragment extends Fragment {


    private TextView textView;
    RadioGroup radioGroup;
    TimePickerDialog timePickerDialog;
    private int targetValue, hour, minute, weightValue;
    String genderValue, userid;
    DatabaseHepler db;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_settingpage, container, false);
        db = new DatabaseHepler(getContext());
        db.getWritableDatabase();
        userid = db.getUserid();

        FirebaseDatabase.getInstance().getReference().child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                textView = view.findViewById(R.id.intakegoal_text);
                textView.setText(dataSnapshot.child("Target").getValue().toString());
                targetValue = Integer.parseInt(dataSnapshot.child("Target").getValue().toString());

                textView = view.findViewById(R.id.gender_text);
                textView.setText(dataSnapshot.child("Gender").getValue().toString());
                genderValue = dataSnapshot.child("Gender").getValue().toString();

                textView = view.findViewById(R.id.weight_text);
                textView.setText(dataSnapshot.child("Weight").getValue().toString() + " kg");
                weightValue = Integer.parseInt(dataSnapshot.child("Weight").getValue().toString());

                textView = view.findViewById(R.id.wake_up_text);
                hour = Integer.parseInt(dataSnapshot.child("Weak_up_time_hour").getValue().toString());
                minute = Integer.parseInt(dataSnapshot.child("Weak_up_time_minute").getValue().toString());
                textView.setText(hour + ":" + minute + " AM");

                textView = view.findViewById(R.id.sleep_text);
                hour = Integer.parseInt(dataSnapshot.child("Sleep_time_hour").getValue().toString());
                minute = Integer.parseInt(dataSnapshot.child("Sleep_time_minute").getValue().toString());
                textView.setText(hour + ":" + minute + " PM");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Database Error", Toast.LENGTH_SHORT).show();
            }
        });

       /* view.findViewById(R.id.reminderSchedule_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ReminderScheduleActivity.class));
            }
        });*/

        view.findViewById(R.id.intakegoal_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView = view.findViewById(R.id.intakegoal_text);
                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.intakegoal_setting);
                textView = dialog.findViewById(R.id.intake_num);
                textView.setText(targetValue + " ml");

                SeekBar seekBar = dialog.findViewById(R.id.seekbar);
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, final int progress, boolean fromUser) {
                        textView = dialog.findViewById(R.id.intake_num);
                        textView.setText(progress + " ml");
                        FirebaseDatabase.getInstance().getReference().child(userid).child("Target").setValue(progress);
                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {}
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) { }
                });
                dialog.findViewById(R.id.okbtn_intakegoal).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        /*view.findViewById(R.id.gender_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView = view.findViewById(R.id.gender_text);
                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.gender_setting);
                radioGroup = dialog.findViewById(R.id.radioGroup_gender);
                if (genderValue == "Male") {
                    RadioButton b = dialog.findViewById(R.id.male);
                    b.setChecked(true);
                } else {
                    RadioButton b = dialog.findViewById(R.id.female);
                    b.setChecked(true);
                }
                dialog.findViewById(R.id.okbtn_gender).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });*/

        view.findViewById(R.id.weight_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.weight_setting);
                NumberPicker numberPicker = dialog.findViewById(R.id.numPicker);
                numberPicker.setMinValue(weightValue);
                numberPicker.setMaxValue(200);
                numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        FirebaseDatabase.getInstance().getReference().child(userid).child("Weight").setValue(newVal);
                    }
                });

                dialog.findViewById(R.id.okbtn_weight).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        view.findViewById(R.id.wake_up_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView = view.findViewById(R.id.wake_up_text);
                timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        FirebaseDatabase.getInstance().getReference().child(userid).child("Weak_up_time_hour").setValue(String.format("%02d", hourOfDay));
                        FirebaseDatabase.getInstance().getReference().child(userid).child("Weak_up_time_minute").setValue(String.format("%02d", minute));
                    }
                }, Calendar.HOUR_OF_DAY, Calendar.MINUTE, false);
                timePickerDialog.show();
            }
        });

        view.findViewById(R.id.sleep_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView = view.findViewById(R.id.sleep_text);
                timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        FirebaseDatabase.getInstance().getReference().child(userid).child("Sleep_time_hour").setValue(String.format("%02d", hourOfDay));
                        FirebaseDatabase.getInstance().getReference().child(userid).child("Sleep_time_minute").setValue(String.format("%02d", minute));
                    }
                }, Calendar.HOUR_OF_DAY, Calendar.MINUTE, false);
                timePickerDialog.show();
            }
        });

        view.findViewById(R.id.privacypolicy_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), PolicyActivity.class));
            }
        });
        return view;
    }

}
