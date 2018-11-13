package com.example.fengtao.mission.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyDAOImpl implements MyDAO {
    
    //MySQLiteOpenHelper openHelper;
    AjaxSQLiteOpenHelper ajaxSQLiteOpenHelper;
    public MyDAOImpl(Context context)
    {
        ajaxSQLiteOpenHelper = new AjaxSQLiteOpenHelper(context);
    }
    public void insert(String result){
        //int count=1;
        
        SQLiteDatabase db = ajaxSQLiteOpenHelper.getWritableDatabase();
        //ContentValues contentValues = new ContentValues();
        Cursor cursor = db.rawQuery("select count(femalename) as count from netname where femalename='"+result+"'",null);
        //db.execSQL("select count(*) from netname where femalename=\"回忆似风不可追\"");
        while(cursor.moveToNext()){
            int count1 = cursor.getInt(cursor.getColumnIndex("count"));

            if(count1 == 0){
                String insertSQL = "Insert into netname(femalename) values ('"+result+"')";
                db.execSQL(insertSQL);
                cursor.close();
                db.close();
            }
        }
        
     
        
        
       // contentValues.put("dbcontent",result);
       // db.insert("scanresult",null,contentValues);
        cursor.close();
        db.close();
    }
    
    public void delete(){
        SQLiteDatabase db = ajaxSQLiteOpenHelper.getWritableDatabase();
        String deleteSQL = "delete from netname";
        db.execSQL(deleteSQL);
        db.close();
    }
    
    public void update(){
        
    }
    
    public String query(){
        List<String> list = new ArrayList<>();
        JSONArray jsonArray = new JSONArray();
        SQLiteDatabase db = ajaxSQLiteOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from netname",null);

        while(cursor.moveToNext()){
/*            int countRaw = cursor.getCount();
            System.out.println(countRaw);*/
            String content = cursor.getString(cursor.getColumnIndex("femalename"));
            System.out.println(cursor.getColumnIndex("femalename"));
            jsonArray.put(content);
            
            //list.add(content);
        }
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("femalename",jsonArray);
        }catch (Exception e){
            e.printStackTrace();
        }
        
        cursor.close();
        db.close();
        return jsonObject.toString();
    }
}
