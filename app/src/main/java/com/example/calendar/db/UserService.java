package com.example.calendar.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class UserService {
    private DBHelper dbHelper;
    private String tbname;
    public UserService(Context context){
        dbHelper = new DBHelper(context);
        tbname = DBHelper.tb_name;
    }
    public void add(Note note){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("tag",note.getTag());
        values.put("title",note.getTitle());
        values.put("text",note.getText());
        values.put("time",note.getTime());
        db.insert(tbname,null,values);
        db.close();
    }
    public void deleteAll(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(tbname,null,null);
        db.close();
    }

    public void delete(int id){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(tbname, "ID=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void update(Note note){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TAG",note.getTag());
        values.put("TITLE",note.getTitle());
        values.put("TEXT", note.getText());
        values.put("TIME", note.getTime());
        db.update(tbname, values, "ID=?", new String[]{String.valueOf(note.getId())});
        db.close();
    }
    public List<Note> listAll(){
        List<Note> noteList = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(tbname,null,null,null,null,null,null);
        if(cursor!=null){
            noteList = new ArrayList<Note>();
            while (cursor.moveToNext()){
                Note note = new Note();
                note.setId(cursor.getInt(cursor.getColumnIndex("ID")));
                note.setTag(cursor.getString(cursor.getColumnIndex("TAG")));
                note.setTitle(cursor.getString(cursor.getColumnIndex("TITLE")));
                note.setText(cursor.getString(cursor.getColumnIndex("TEXT")));
                note.setTime(cursor.getString(cursor.getColumnIndex("TIME")));
                noteList.add(note);
            }
            cursor.close();
        }
        db.close();
        return noteList;
    }
    public List<Note> listStar(){
        List<Note> noteList = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
//        String sql = "SELECT * FROM "+tbname+" WHERE TAG = ? ORDER BY TO_DATE(TIME,'yyyy-mm-dd hh24:mm')";
//        Cursor cursor = db.rawQuery(sql,new String[]{"1"});
        Cursor cursor = db.query(tbname,null,"TAG = ?",new String[]{String.valueOf(1)},null,null,null);
        if(cursor!=null){
            noteList = new ArrayList<Note>();
            while (cursor.moveToNext()){
                Note note = new Note();
                note.setId(cursor.getInt(cursor.getColumnIndex("ID")));
                note.setTag(cursor.getString(cursor.getColumnIndex("TAG")));
                note.setTitle(cursor.getString(cursor.getColumnIndex("TITLE")));
                note.setText(cursor.getString(cursor.getColumnIndex("TEXT")));
                note.setTime(cursor.getString(cursor.getColumnIndex("TIME")));
                noteList.add(note);
            }
            cursor.close();
        }
        db.close();
        return noteList;
    }
}
