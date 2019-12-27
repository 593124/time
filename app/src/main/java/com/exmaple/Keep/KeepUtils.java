package com.exmaple.Keep;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;


public class KeepUtils {



    private static KeepUtils dao;

    private SQLiteDatabase db;

    private KeepUtils(Context context) {
        SqliteHelper dbHelper = new SqliteHelper(context, "keep.db", null, 1);
        db = dbHelper.getWritableDatabase();
    }


    public  static KeepUtils getInstance(Context context) {
        if (dao == null) {
            dao = new KeepUtils(context);
        }
        return dao;
    }




   public void insertNote(Keep order) {
           ContentValues cv = new ContentValues();
           cv.put("title", order.title);
           cv.put("des", order.des);
           cv.put("time", order.time);
           cv.put("period", order.period);
           cv.put("pic", order.pic);
           db.insert(SqliteHelper.TABLE_NAME, null, cv);
    }
    public void deleteKeepById(String id) {
        db.delete(SqliteHelper.TABLE_NAME, "id" + "=?" , new String[]{id});
    }
    public void changeKeep(Keep order) {
        ContentValues cv = new ContentValues();
        cv.put("title", order.title);
        cv.put("des", order.des);
        cv.put("time", order.time);
        cv.put("period", order.period);
        cv.put("pic", order.pic);
        db.update(SqliteHelper.TABLE_NAME,cv, "id" + "=?", new String[]{order.id+""});
    }

    public List<Keep> loadKeepList(){
        ArrayList<Keep> keeperList = new ArrayList<Keep>();
        Cursor cursor = db.query(SqliteHelper.TABLE_NAME, null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String des = cursor.getString(cursor.getColumnIndex("des"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                String period = cursor.getString(cursor.getColumnIndex("period"));
                String pic = cursor.getString(cursor.getColumnIndex("pic"));

                Keep keep = new Keep();
                keep.id = id;
                keep.title = title;
                keep.des = des;
                keep.time = time;
                keep.period = period;
                keep.pic = pic;
                keeperList.add(keep);
            }

            cursor.close();
        }
        return keeperList;
    }

}
