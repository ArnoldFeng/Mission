package com.example.fengtao.mission.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class MyDAOImpl implements MyDAO {
    
    MySQLiteOpenHelper openHelper;
    
    public MyDAOImpl(Context context){
        openHelper = new MySQLiteOpenHelper(context);
    }
    public void insert(){
        SQLiteDatabase db = openHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name","阿诺");
        contentValues.put("tel","123456789");
        db.insert("user",null,contentValues);
        db.close();
    }
    
    public void delete(){
        
    }
    
    public void update(){
        
    }
    
    public void query(){
        
    }
}
