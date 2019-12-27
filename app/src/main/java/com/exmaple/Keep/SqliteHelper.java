package com.exmaple.Keep;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;


public class SqliteHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "keeper";
    public SqliteHelper(Context context, String name, CursorFactory factory,
                        int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table keeper ("
                + "id integer primary key autoincrement, "
                + "title VARCHAR, "
                + "des VARCHAR, "
                + "period VARCHAR, "
                + "time VARCHAR, "
                + "pic VARCHAR)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
