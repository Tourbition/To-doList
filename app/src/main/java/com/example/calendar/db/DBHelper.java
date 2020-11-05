package com.example.calendar.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String db_name = "note1.db";
    public static final String tb_name = "tb_note";

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,version);

    }
    public DBHelper(Context context){
        super(context,db_name,null,VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql1 = "CREATE TABLE "+tb_name+"(ID INTEGER PRIMARY KEY AUTOINCREMENT, TAG varchar(5) ,TITLE varchar(15), TEXT varchar(100),TIME varchar(30));";
        db.execSQL(sql1);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
