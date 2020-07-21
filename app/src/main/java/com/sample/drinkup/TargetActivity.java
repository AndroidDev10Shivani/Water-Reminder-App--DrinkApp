package com.sample.drinkup;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TargetActivity extends AppCompatActivity {

    private TextView textView_name, textView_weight, textView_weak, textView_sleep, textView_target, textView_glass;
    String userid;
    private int hour, minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target);

        textView_name = findViewById(R.id.name_text);
        textView_weight = findViewById(R.id.weight_text);
        textView_weak = findViewById(R.id.wake_up_text);
        textView_sleep = findViewById(R.id.sleep_text);
        textView_target = findViewById(R.id.intake_text);
        textView_glass = findViewById(R.id.glass_text);

        Bundle bundle = getIntent().getExtras();
        userid = bundle.getString("id");

        FirebaseDatabase.getInstance().getReference().child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                textView_name.setText(dataSnapshot.child("Name").getValue().toString());
                textView_weight.setText(dataSnapshot.child("Weight").getValue().toString()+" kg");

                hour = Integer.parseInt(dataSnapshot.child("Weak_up_time_hour").getValue().toString());
                minute = Integer.parseInt(dataSnapshot.child("Weak_up_time_minute").getValue().toString());
                textView_weak.setText(hour+":"+minute+" AM");

                hour = Integer.parseInt(dataSnapshot.child("Sleep_time_hour").getValue().toString());
                minute = Integer.parseInt(dataSnapshot.child("Sleep_time_minute").getValue().toString());
                textView_sleep.setText(hour+":"+minute+" PM");

                textView_glass.setText(dataSnapshot.child("Glass").getValue().toString());
                textView_target.setText(dataSnapshot.child("Target").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Database Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onConfirmButton(View view) {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
}

