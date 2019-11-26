package com.example.glucose;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Glucose.db";

    private long ins;

    private static final String TABLE_NAME = "Person";

    private static final String COL_1 = "row_id";
    private static final String COL_3 = "glucose";
    private static final String COL_2 = "name";
    private static final String COL_4 = "weight";
    private static final String COL_5 = "time1";
    private static final String COL_6 = "time2";
    private static final String COL_7 = "glucose_2";


    private static final String create_table = "Create TABLE "+ TABLE_NAME + "(" + COL_1 +" INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_2 + " TEXT, " +COL_3 + " REAL, " + COL_4 +" REAL, " + COL_5 + " REAL, " + COL_6 + " REAL, " + COL_7 + " REAL)";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(create_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("Drop table if exists Person");
    }
    public boolean insert_user(String name, double weight, double glucose, double time1, double time2, double glucose2){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, name);
        contentValues.put(COL_4, weight);
        contentValues.put(COL_3, glucose);
        contentValues.put(COL_5, time1);
        contentValues.put(COL_6, time2);
        contentValues.put(COL_7, glucose2);
        ins = db.insert(TABLE_NAME,null,contentValues);
        Log.d("TAG","data inserted "+ins);

        return true;
    }
    public boolean update_user(long row_id, double glucose, double weight, double time1, double time2, double glucose2){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues newContentValues = new ContentValues();
        newContentValues.put(COL_3, glucose);
        newContentValues.put(COL_5, time1);
        newContentValues.put(COL_6, time2);
        newContentValues.put(COL_4, weight);
        newContentValues.put(COL_7, glucose2);
        String where = COL_1 + "=" + row_id;
        long ins_new = db.update(TABLE_NAME, newContentValues, where, null);
        Log.d("TAG", "data_updated" + ins_new);
        return true;
    }
    public Cursor getdata_user() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("Select * from Person", null);
        return res;
    }
}
