package com.compet.petdoc.manager;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.compet.petdoc.data.HospitalItem;

/**
 * Created by Mu on 2016-11-24.
 */

public class DBManager extends SQLiteOpenHelper {

    private static final String TAG = DBManager.getInstance().getClass().getSimpleName();

    private ContentValues values = new ContentValues();

    private static DBManager instance;

    public static DBManager getInstance() {
        if (instance == null) {
            instance = new DBManager();
        }
        return instance;
    }

    private static final String DB_NAME = "hospital_db";

    private static final int DB_VERSION = 1;

    private DBManager() {
        super(MyApplication.getContext(), DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + HospitalItem.HospitalInfo.TABLE
                     + "("
                     + HospitalItem.HospitalInfo._ID
                     + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                     + HospitalItem.HospitalInfo.COLUMN_NAME
                     + " TEXT,"
                     + HospitalItem.HospitalInfo.COLUMN_LOCATION
                     + " TEXT);"
                     + HospitalItem.HospitalInfo.COLUMN_PHONE_NUMBER
                     + " TEXT);";
        db.execSQL(sql);
    }

    public Cursor getBookmark() {
        String sql = "SELECT " + "* " + "FROM " + HospitalItem.HospitalInfo.TABLE;
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery(sql, null);

    }

    public long addBookmark(HospitalItem hospitalItem) {

        SQLiteDatabase db = getWritableDatabase();
        values.clear();
        values.put(HospitalItem.HospitalInfo.COLUMN_NAME, hospitalItem.getHosName());
        values.put(HospitalItem.HospitalInfo.COLUMN_LOCATION, hospitalItem.getAddress());
        values.put(HospitalItem.HospitalInfo.COLUMN_PHONE_NUMBER, hospitalItem.getPhoneNumber());

        Log.d(TAG, "DB 입력 성공 : " + hospitalItem.getHosName());
        return db.insert(HospitalItem.HospitalInfo.TABLE, null, values);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
