package com.sample.drinkup;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import me.itangqi.waveloadingview.WaveLoadingView;

public class MainFragment extends Fragment {
    int count = 0, progress = 0;
    int glasses, fill_progress;
    private List<String> labeltext;
    private Handler mainhandler = new Handler();
    WaveLoadingView waveLoadingView;

    TextView textView_total, textView_tofill, textView_label;

    private ListView listView;
    private ArrayList<DataModel> arrayList;
    RecordAdapter adapter;
    DatabaseHepler db;

    String time, ampm, userid;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_mainpage, container, false);
        waveLoadingView = view.findViewById(R.id.waveloadingview);
        textView_total = view.findViewById(R.id.totalml);
        textView_tofill = view.findViewById(R.id.tofillml);
        textView_label = view.findViewById(R.id.labeltext);
        view.findViewById(R.id.achive).setVisibility(View.INVISIBLE);

        db = new DatabaseHepler(getContext());
        db.getWritableDatabase();

        userid = db.getUserid();

        listView = view.findViewById(R.id.listview_record);
        arrayList = new ArrayList<>();

        new Thread(new LabelRunnable()).start();

        FirebaseDatabase.getInstance().getReference().child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                glasses = Integer.parseInt(dataSnapshot.child("Glass").getValue().toString());
                textView_total.setText(dataSnapshot.child("Target").getValue().toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Database Error", Toast.LENGTH_SHORT).show();
            }
        });

        view.findViewById(R.id.glass).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count = count + 250;
                final int glass = glasses;
                fill_progress = 100 / glasses;

                loadWater();

                if(Integer.parseInt(textView_tofill.getText().toString())>=Integer.parseInt(textView_total.getText().toString()))
                {
                    view.findViewById(R.id.achive).setVisibility(View.VISIBLE);
                }

                Calendar calendar = Calendar.getInstance();
                int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                int currentMinute = calendar.get(Calendar.MINUTE);
                if (currentHour >= 12) {
                    ampm = "PM";
                } else {
                    ampm = "AM";
                }
                time = (String.format("%02d:%02d",currentHour, currentMinute)+ ampm);
                db.addtimeData(userid, time);
                loadDataInListView();
            }
        });

        view.findViewById(R.id.idea).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), TipActivity.class));
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                arrayList.remove(position);
                adapter.notifyDataSetChanged();
                count-=250;
                loadWater();
                adapter.notifyDataSetChanged();
                return true;
            }
        });

        return view;
    }
    private void loadWater() {
        progress = progress + fill_progress;
        waveLoadingView.setProgressValue(progress);
        if (progress < 40) {
            waveLoadingView.setBottomTitle(String.format("%d", count));
            waveLoadingView.setCenterTitle("");
            waveLoadingView.setTopTitle("");
        } else if (progress < 70) {
            waveLoadingView.setBottomTitle("");
            waveLoadingView.setCenterTitle(String.format("%d", count));
            waveLoadingView.setTopTitle("");
        } else {
            waveLoadingView.setBottomTitle("");
            waveLoadingView.setCenterTitle("");
            waveLoadingView.setTopTitle(String.format("%d", count));
        }
        FirebaseDatabase.getInstance().getReference().child(userid).child("FillTarget").setValue(count);
        FirebaseDatabase.getInstance().getReference().child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                textView_tofill.setText(dataSnapshot.child("FillTarget").getValue().toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Database Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void loadDataInListView() {
        arrayList = db.getAllData();
        adapter = new RecordAdapter(getContext(), arrayList);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    class LabelRunnable implements Runnable {
        @Override
        public void run() {

            labeltext = new ArrayList<>();
            labeltext.add("Drink your glass of water slowly with some small sips");
            labeltext.add("Hold the water in your mouth for a while before swallowing");
            labeltext.add("Drinking water in a sitting posture is better than in a standing or running position");
            labeltext.add("Do not drink cold water or water with ice");
            labeltext.add("Do not drink water immediately after eating");
            labeltext.add("Do not drink water immediately after hot drinks like tea or coffee");
            labeltext.add("Always drink water before urinating and do not drink water immediately after urinating");

            final Iterator iterator = labeltext.iterator();
            while (iterator.hasNext()) {
                mainhandler.post(new Runnable() {
                    @Override
                    public void run() {
                        textView_label.setText(iterator.next().toString());
                    }
                });
                try {
                    Thread.sleep(7200000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
