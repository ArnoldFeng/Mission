package com.example.fengtao.mission.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteOpenHelper extends SQLiteOpenHelper{
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "missionone";
    String SQL = "CREATE TABLE scanresult(_id integer primary key autoincrement, dbcontent TEXT)";
    
    public MySQLiteOpenHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    
    public void onCreate(SQLiteDatabase db){
        db.execSQL(SQL);
    }
    
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
        
    }
}

