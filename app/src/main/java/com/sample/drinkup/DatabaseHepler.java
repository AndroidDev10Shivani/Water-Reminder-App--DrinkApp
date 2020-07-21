package com.sample.drinkup;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseHepler extends SQLiteOpenHelper {
    private static final String databasename = "drinkApp.db";
    private static final int databaseVersion = 1;
    private static final String tablename = "drinkApp_table";
    private Context context;

    public DatabaseHepler(@Nullable Context context) {
        super(context, databasename, null, databaseVersion);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + tablename + "(id INTEGER PRIMARY KEY AUTOINCREMENT, userid TEXT, time TEXT)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "DROP TABLE IF EXISTS " + tablename;
        db.execSQL(query);
        onCreate(db);
    }

    public void saveData(String userid, String time) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String query = "DROP TABLE IF EXISTS " + tablename;
        sqLiteDatabase.execSQL(query);
        onCreate(sqLiteDatabase);
        ContentValues contentValues = new ContentValues();
        contentValues.put("userid", userid);
        contentValues.put("time", time);
        sqLiteDatabase.insert(tablename, null, contentValues);
    }

    public void addtimeData(String userid, String time) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("userid", userid);
        contentValues.put("time", time);
        sqLiteDatabase.insert(tablename, null, contentValues);
    }

    public String getUserid() {
        String query = "SELECT userid FROM " + tablename;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        String userid = "NA";
        if (cursor != null) {
            cursor.moveToFirst();
            userid = cursor.getString(cursor.getColumnIndex("userid"));
        }
        cursor.close();
        return userid;
    }

    public ArrayList<DataModel> getAllData() {
        String query = "SELECT time FROM " + tablename + " WHERE time is not null";
        ArrayList<DataModel> arrayList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        while (cursor.moveToNext())
        {
            String time = cursor.getString(cursor.getColumnIndex("time"));
            DataModel dataModel = new DataModel(time);
            arrayList.add(dataModel);
        }
        return arrayList;
    }
}
