package com.sample.drinkup;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class TipActivity extends AppCompatActivity {

    ListView listView;
    private List<Integer> imgtext;
    private List<Integer> images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.colorBlue));

        setContentView(R.layout.activity_tip);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("How to drink water correctly?");
        setSupportActionBar(toolbar);

        listView = findViewById(R.id.listview);

        imgtext = new ArrayList<>();
        imgtext.add(R.string.text1);
        imgtext.add(R.string.text2);
        imgtext.add(R.string.text3);
        imgtext.add(R.string.text4);
        imgtext.add(R.string.text5);
        imgtext.add(R.string.text6);
        imgtext.add(R.string.text7);

        images = new ArrayList<>();
        images.add(R.drawable.img1);
        images.add(R.drawable.img2);
        images.add(R.drawable.img3);
        images.add(R.drawable.img4);
        images.add(R.drawable.img5);
        images.add(R.drawable.img6);
        images.add(R.drawable.img7);

        final DemoViewAdapter demo = new DemoViewAdapter();
        listView.setAdapter(demo);
    }

    public void backButton(View view) {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }

    class DemoViewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return imgtext.size();
        }

        @Override
        public Object getItem(int position) {
            return imgtext.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = getLayoutInflater();
            View v = layoutInflater.inflate(R.layout.listview_rows, null);

            ImageView imageView = v.findViewById(R.id.tipimg);
            imageView.setImageResource(images.get(position));

            TextView textView = v.findViewById(R.id.tiptext);
            textView.setText(imgtext.get(position));

            return v;
        }
    }
}
