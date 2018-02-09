package com.example.jomer.filetracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by james on 3/1/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";


    private static final String TABLE_NAME = "people_table";
    private static final String COL1 = "ID";
    private static final String COL2 = "name";
    private static final String TABLE_NAME1 = "android_table";
    private static final String TOL1 = "ID";
    private static final String TOL2 = "name";
    private static final String TABLE_NAME2 = "android_table1";
    private static final String vOL1 = "ID";
    private static final String vOL2 = "name";
    private static final String TABLE_NAME3 = "android_table2";
    private static final String cOL1 = "ID";
    private static final String cOL2 = "name";

    public DatabaseHelper(Context context) {
        super(context, TABLE_NAME, null, 1);


    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL2 +" TEXT)";
        String createTable1 = "CREATE TABLE " + TABLE_NAME1 + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TOL2 +" TEXT)";
        String createTable2 = "CREATE TABLE " + TABLE_NAME2 + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                vOL2 +" TEXT)";
        String createTable3 = "CREATE TABLE " + TABLE_NAME3 + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                cOL2 +" TEXT)";
        db.execSQL(createTable);
        db.execSQL(createTable1);
        db.execSQL(createTable2);
        db.execSQL(createTable3);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME);
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME1);
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME2);
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME3);
        onCreate(db);
    }

    public boolean addData(String item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, item);
        Log.d(TAG, "addData: Adding " + item + " to " + TABLE_NAME);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean addData1(String item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TOL2, item);
        Log.d(TAG, "addData1: Adding " + item + " to " + TABLE_NAME1);
        long result = db.insert(TABLE_NAME1, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public boolean addData2(String item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(vOL2, item);
        Log.d(TAG, "addData2: Adding " + item + " to " + TABLE_NAME2);
        long result = db.insert(TABLE_NAME2, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public boolean addData3(String item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(cOL2, item);
        Log.d(TAG, "addData3: Adding " + item + " to " + TABLE_NAME3);
        long result = db.insert(TABLE_NAME3, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public Cursor getDa(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME +"WHERE " + COL2 + " = '" + name + "'";;
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public Cursor getData1(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME1;
        Cursor data1 = db.rawQuery(query, null);
        return data1;
    }
    public Cursor getData2(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME2;
        Cursor data2 = db.rawQuery(query, null);
        return data2;
    }
    public Cursor getData3(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME3;
        Cursor data3 = db.rawQuery(query, null);
        return data3;
    }

    public Cursor getItemID(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + COL1 + " FROM " + TABLE_NAME +
                " WHERE " + COL2 + " = '" + name + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }


    public void deleteName(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE "
                + COL2 + " = '" + name + "'";
        Log.d(TAG, "deleteName: query: " + query);
        Log.d(TAG, "deleteName: Deleting " + name + " from database.");
        db.execSQL(query);
    }
    public void deleteName1(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME1 + " WHERE "
                + TOL2 + " = '" + name + "'";
        Log.d(TAG, "deleteName: query: " + query);
        Log.d(TAG, "deleteName: Deleting " + name + " from database.");
        db.execSQL(query);
    }



}

