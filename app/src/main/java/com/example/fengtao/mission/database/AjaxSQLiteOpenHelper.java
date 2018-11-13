package com.example.fengtao.mission.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AjaxSQLiteOpenHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "missionone";
    public String sql="CREATE TABLE netname(_id integer primary key autoincrement,femalename STRING)";
    
    public AjaxSQLiteOpenHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    
    public void onCreate(SQLiteDatabase db){
        db.execSQL(sql);
    }
    
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
        
    }
}
