package com.example.asus.munmestsa0_1.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Asus on 2.12.2016.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Metsa.db";
    public static final String TABLE_METSA_ID = "metsaID_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "METSA_ID";

    public static final String TABLE_USER = "user_table";
    public static final String USER_COL_1 = "ID";
    public static final String USER_COL_2 = "USER_NAME";



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_METSA_ID + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, METSA_ID STRING)");
        db.execSQL("create table " + TABLE_USER + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, USER_NAME STRING)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROB TABLE IF EXISTS " +TABLE_METSA_ID);
        db.execSQL("DROB TABLE IF EXISTS " +TABLE_USER);
        onCreate(db);
    }
    public boolean insertData(String metsaID){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, metsaID);
        long result = db.insert(TABLE_METSA_ID, null, contentValues);
        if(result== -1)
            return  false;
        else
            return true;
    }
    public boolean insertUsername(String u){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_COL_2, u);
        long result = db.insert(TABLE_USER, null, contentValues);
        if(result== -1)
            return  false;
        else
            return true;
    }

    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_METSA_ID,null);
        return res;
    }
    public Cursor getUsername(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_USER,null);
        return res;

    }
    public Cursor getMetsa(String metsaName){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] projections = {COL_1};
        String selection = "NAME"+" LIKE ?";
        String[] selection_args = {metsaName};
        Cursor cursor = db.query(TABLE_METSA_ID, projections, selection, selection_args, null,null,null);

        return cursor;

    }
}
