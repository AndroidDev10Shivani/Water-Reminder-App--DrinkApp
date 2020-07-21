package com.sample.drinkup;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GeneralActivity extends AppCompatActivity implements NumberPicker.OnValueChangeListener {
    private int pageCount = 0;
    private ImageView imageView_tag, imageView_person;
    private EditText editText_name;
    private TextView textView_male, textView_female;
    String userid, gender, time=null;
    DatabaseHepler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlue));
        setContentView(R.layout.activity_general);

        db = new DatabaseHepler(this);
        db.getWritableDatabase();

        findViewById(R.id.Layout1).setVisibility(View.VISIBLE);
        findViewById(R.id.linearLayout1).setVisibility(View.GONE);
        findViewById(R.id.relativeLayout1).setVisibility(View.GONE);
        findViewById(R.id.Layout2).setVisibility(View.GONE);
        findViewById(R.id.Layout3).setVisibility(View.GONE);
        findViewById(R.id.Layout4).setVisibility(View.GONE);
        findViewById(R.id.Layout5).setVisibility(View.GONE);

        editText_name = findViewById(R.id.username);
        textView_male = findViewById(R.id.maletxt);
        textView_female = findViewById(R.id.femaletext);

        imageView_person = findViewById(R.id.imgboy);
        imageView_person.setImageResource(R.drawable.boy);
        imageView_person = findViewById(R.id.imggirl);
        imageView_person.setImageResource(R.drawable.girl);

        findViewById(R.id.cardmale).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userid = "User " + editText_name.getText().toString();
                db.saveData(userid,time);
                FirebaseDatabase.getInstance().getReference(userid).child("Name").setValue(editText_name.getText().toString());

                textView_male.setTextColor(Color.parseColor("#03A9F4"));
                textView_female.setTextColor(Color.parseColor("#B8B2B2"));
                findViewById(R.id.imgboy).setAlpha(0.9f);
                findViewById(R.id.imggirl).setAlpha(0.2f);
                gender = "Male";
                FirebaseDatabase.getInstance().getReference(userid).child("Gender").setValue(gender);
                waterPlan(70);
            }
        });
        findViewById(R.id.cardfemale).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userid = "User " + editText_name.getText().toString();
                db.saveData(userid, time);
                FirebaseDatabase.getInstance().getReference(userid).child("Name").setValue(editText_name.getText().toString());

                textView_male.setTextColor(Color.parseColor("#B8B2B2"));
                textView_female.setTextColor(Color.parseColor("#03A9F4"));
                findViewById(R.id.imgboy).setAlpha(0.2f);
                findViewById(R.id.imggirl).setAlpha(0.9f);
                gender = "Female";
                FirebaseDatabase.getInstance().getReference(userid).child("Gender").setValue(gender);
                waterPlan(60);
            }
        });
        NumberPicker numberPicker = findViewById(R.id.weightPicker);
        numberPicker.setMinValue(30);
        numberPicker.setMaxValue(200);
        numberPicker.setOnValueChangedListener(this);

        TimePicker wake_timePicker = findViewById(R.id.weak_up_timer);
        wake_timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                FirebaseDatabase.getInstance().getReference(userid).child("Weak_up_time_hour").setValue(String.format("%02d",hourOfDay));
                FirebaseDatabase.getInstance().getReference(userid).child("Weak_up_time_minute").setValue(String.format("%02d",minute));
            }
        });

        TimePicker sleep_timePicker = findViewById(R.id.sleepy_timer);
        sleep_timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                FirebaseDatabase.getInstance().getReference(userid).child("Sleep_time_hour").setValue(String.format("%02d",hourOfDay));
                FirebaseDatabase.getInstance().getReference(userid).child("Sleep_time_minute").setValue(String.format("%02d",minute));
            }
        });
    }

    public void onLetsGoButton(View view) {
        onNextButton(view);
    }

    @SuppressLint("ResourceAsColor")
    public void onNextButton(View view) {

        if (pageCount == 0) {
            findViewById(R.id.linearLayout1).setVisibility(View.VISIBLE);
            findViewById(R.id.relativeLayout1).setVisibility(View.VISIBLE);
            findViewById(R.id.Layout1).setVisibility(View.GONE);
            findViewById(R.id.prevbutton).setVisibility(View.INVISIBLE);
            findViewById(R.id.Layout2).setVisibility(View.VISIBLE);
            if (editText_name.length() == 0) {
                Toast toast = Toast.makeText(this, "Your First Name is Mandatory", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                View toastView = toast.getView();
                toastView.setBackgroundResource(R.drawable.toast_drawable);
                toast.show();
            } else {
                pageCount++;
                findViewById(R.id.prevbutton).setVisibility(View.VISIBLE);
                imageView_tag = findViewById(R.id.tag1);
                imageView_tag.setImageResource(R.drawable.ic_label_blue);
                findViewById(R.id.Layout2).setVisibility(View.GONE);
                findViewById(R.id.Layout3).setVisibility(View.VISIBLE);
                imageView_person = findViewById(R.id.weight_person);
                if (gender == "Male") {
                    imageView_person.setImageResource(R.drawable.weight_boy);
                } else {
                    imageView_person.setImageResource(R.drawable.weight_girl);
                }
                pageCount++;
            }
        } else if (pageCount == 2) {
            findViewById(R.id.prevbutton).setVisibility(View.VISIBLE);
            imageView_tag = findViewById(R.id.tag2);
            imageView_tag.setImageResource(R.drawable.ic_label_blue);
            findViewById(R.id.Layout3).setVisibility(View.GONE);
            findViewById(R.id.Layout4).setVisibility(View.VISIBLE);
            findViewById(R.id.Layout5).setVisibility(View.GONE);

            imageView_person = findViewById(R.id.weak_up_person);
            if (gender == "Male") {
                imageView_person.setImageResource(R.drawable.weak_up_boy);
            } else {
                imageView_person.setImageResource(R.drawable.weak_up_girl);
            }
            pageCount++;
        } else if (pageCount == 3) {
            imageView_tag = findViewById(R.id.tag3);
            imageView_tag.setImageResource(R.drawable.ic_label_blue);
            findViewById(R.id.Layout4).setVisibility(View.GONE);
            findViewById(R.id.Layout5).setVisibility(View.VISIBLE);

            imageView_person = findViewById(R.id.sleepy_person);
            if (gender == "Male") {
                imageView_person.setImageResource(R.drawable.sleepy_boy);
            } else {
                imageView_person.setImageResource(R.drawable.sleepy_girl);
            }
            pageCount++;
        } else if (pageCount == 4) {
            imageView_tag = findViewById(R.id.tag4);
            imageView_tag.setImageResource(R.drawable.ic_label_blue);
            pageCount--;
            startActivity(new Intent(getApplicationContext(), TargetActivity.class).putExtra("id", userid));
        }
    }

    public void onBackButton(View view) {

        if (pageCount == 3) {
            findViewById(R.id.prevbutton).setVisibility(View.VISIBLE);
            findViewById(R.id.Layout5).setVisibility(View.GONE);
            findViewById(R.id.Layout4).setVisibility(View.VISIBLE);
            pageCount--;
        } else if (pageCount == 2) {
            findViewById(R.id.prevbutton).setVisibility(View.VISIBLE);
            findViewById(R.id.Layout4).setVisibility(View.GONE);
            findViewById(R.id.Layout3).setVisibility(View.VISIBLE);
            pageCount--;
        } else if (pageCount == 1) {
            findViewById(R.id.prevbutton).setVisibility(View.INVISIBLE);
            findViewById(R.id.Layout3).setVisibility(View.GONE);
            findViewById(R.id.Layout2).setVisibility(View.VISIBLE);
            pageCount--;
        }
    }

    public void waterPlan(int weight) {
        FirebaseDatabase.getInstance().getReference(userid).child("Weight").setValue(weight);
        FirebaseDatabase.getInstance().getReference(userid).child("Weak_up_time_hour").setValue(6);
        FirebaseDatabase.getInstance().getReference(userid).child("Weak_up_time_minute").setValue(00);
        FirebaseDatabase.getInstance().getReference(userid).child("Sleep_time_hour").setValue(10);
        FirebaseDatabase.getInstance().getReference(userid).child("Sleep_time_minute").setValue(30);
        int glass = (weight / 2) / 8 + 1;
        FirebaseDatabase.getInstance().getReference(userid).child("Glass").setValue(glass);
        int target = glass * 250;
        FirebaseDatabase.getInstance().getReference(userid).child("Target").setValue(target);
        FirebaseDatabase.getInstance().getReference(userid).child("Profile").setValue(0);
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        waterPlan(newVal);
    }

    public void onSkipButton(View view) {
        userid = db.getUserid();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
}
