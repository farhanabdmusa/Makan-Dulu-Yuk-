package com.robot.mr.makanduluyuk;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "aktivitas.db";
    public static final String TABLE_NAME = "aktivitas_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "Kegiatan";
    public static final String COL_3 = "Tanggal";
    public static final String COL_4 = "Jam_mulai";
    public static final String COL_5 = "Jam_akhir";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,KEGIATAN TEXT,TANGGAL TEXT,JAM_MULAI TEXT,JAM_AKHIR TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String kegiatan, String tanggal, String jam_mulai, String jam_akhir){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, kegiatan);
        contentValues.put(COL_3, tanggal);
        contentValues.put(COL_4, jam_mulai);
        contentValues.put(COL_5, jam_akhir);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if(result == -1){
            return false;
        } else{
            return true;
        }
    }

    public Cursor getDataSekarang(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select *" +
                                      " from " + TABLE_NAME +
                                      " where "+ COL_3 + " IN (select strftime('%d/%m/%Y','now'))" +
                                      " order by " + COL_4 + " asc", null);
        return res;
    }


    public Cursor getDataKemarin(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select *" +
                " from " + TABLE_NAME +
                " where "+ COL_3 + " IN (select strftime('%d/%m/%Y','now', '-1 day'))" +
                " order by " + COL_4 + " asc", null);
        return res;
    }

    public Cursor getDataBesok(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select *" +
                " from " + TABLE_NAME +
                " where "+ COL_3 + " IN (select strftime('%d/%m/%Y','now', '+1 day'))" +
                " order by " + COL_4 + " asc", null);
        return res;
    }

//    db.rawQuery("select * from " + TABLE_NAME + "where " +" order by "+ COL_4 +" asc", null);

//    public Cursor sortData(){
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor hasil = db.rawQuery("select " + COL_4 +","+ COL_5 +" from " + TABLE_NAME +" order by " + COL_4 +" ASC", null);
//        return hasil;
//    }
}
