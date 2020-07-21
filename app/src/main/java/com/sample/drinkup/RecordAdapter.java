package com.sample.drinkup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class RecordAdapter extends BaseAdapter {

    Context context;
    ArrayList<DataModel> arrayList;

    public RecordAdapter(@NonNull Context context, ArrayList<DataModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }
    @Override
    public int getCount() {
        return this.arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.list_daily_record,null);

        TextView hmtext = (TextView) convertView.findViewById(R.id.time);

        DataModel dataModel = arrayList.get(position);
        hmtext.setText(dataModel.getHmtime());

        return convertView;
    }
}
