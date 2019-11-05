package com.icesoft.magnetlinksearch.sqlites;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Base64;
import com.icesoft.magnetlinksearch.models.Result;

import java.util.ArrayList;
import java.util.List;

public class ResultDao {
    private static final String TAG = "ResultDao";
    SQLiteDbHelper helper = null;
    public static final String QUEST = " = ? ";
    public ResultDao(Context context){
        helper = new SQLiteDbHelper(context);
    }
    public void add(List<Result> results) {
        SQLiteDatabase db = helper.getWritableDatabase();
        Result result = null;
        db.beginTransaction();
        int len = results.size();
        for (int i = 0; i < len; i++) {
            result = results.get(i);
            db.execSQL("INSERT INTO "+ SQLiteDbHelper.RESULT_TABLE_NAME +
                    " (" +
                        "`" + SQLiteDbHelper.RESULT_INFOHASH   + "`," +
                        "`" + SQLiteDbHelper.RESULT_DATE       + "`," +
                        "`" + SQLiteDbHelper.RESULT_NAME       + "`," +
                        "`" + SQLiteDbHelper.RESULT_SIZE       + "`," +
                        "`" + SQLiteDbHelper.RESULT_FILES      + "`" +

                    ") VALUES " +
                    "(" +
                        "'" + result.id     + "'," +
                        "'" + result.date   + "'," +
                        "'" + result.name   + "'," +
                        "'" + result.size   + "'," +
                        "'" + result.count  + "' " +
                    ")");
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }
    public void add(Result result) {
        if(result !=null){
            String name64 = Base64.encodeToString(result.name.getBytes(), Base64.DEFAULT);
            SQLiteDatabase db = helper.getWritableDatabase();
            db.beginTransaction();
            db.execSQL("INSERT INTO "+ SQLiteDbHelper.RESULT_TABLE_NAME +
                    " (" +
                        "`" + SQLiteDbHelper.RESULT_INFOHASH   + "`," +
                        "`" + SQLiteDbHelper.RESULT_DATE       + "`," +
                        "`" + SQLiteDbHelper.RESULT_NAME       + "`," +
                        "`" + SQLiteDbHelper.RESULT_SIZE       + "`," +
                        "`" + SQLiteDbHelper.RESULT_FILES      + "`" +
                    ") VALUES " +
                    "(" +
                        "'" + result.id     + "'," +
                        "'" + result.date   + "'," +
                        "'" + name64        + "'," +
                        "'" + result.size   + "'," +
                        "'" + result.count  + "' " +
                    ")");
            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();
        }
    }
    public boolean exist(String infohash){
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.query(SQLiteDbHelper.RESULT_TABLE_NAME,
                new String[]{
                        SQLiteDbHelper.RESULT_INFOHASH
                },SQLiteDbHelper.RESULT_INFOHASH + " = ? ",new String[]{infohash},"","","");
        boolean isExist = c.moveToFirst();
        c.close();
        db.close();
        return isExist;
    }
    public boolean delete(String infohash){
        SQLiteDatabase db = helper.getWritableDatabase();
        int r = db.delete(SQLiteDbHelper.RESULT_TABLE_NAME, SQLiteDbHelper.RESULT_INFOHASH + QUEST, new String[]{infohash});
        db.close();
        return r==1;
    }
    public int count(){
        int size = 0;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.query(SQLiteDbHelper.RESULT_TABLE_NAME,
                new String[]{"count(id)"},
                "",new String[]{},"","","");
        if(c.moveToFirst()){
            size = c.getInt(0);
        }
        c.close();
        db.close();
        return size;
    }
    public List<Result> load(int from,int limit){
        List<Result> results = null;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.query(SQLiteDbHelper.RESULT_TABLE_NAME,
                new String[]{
                        SQLiteDbHelper.RESULT_ID,
                        SQLiteDbHelper.RESULT_INFOHASH,
                        SQLiteDbHelper.RESULT_DATE,
                        SQLiteDbHelper.RESULT_NAME,
                        SQLiteDbHelper.RESULT_SIZE,
                        SQLiteDbHelper.RESULT_FILES
                },"",new String[]{},"","","",
                 from + "," + limit);
        if(c.moveToFirst()){
            results = new ArrayList<>();
            do{
                String infohash = c.getString(c.getColumnIndex(SQLiteDbHelper.RESULT_INFOHASH));
                String date     = c.getString(c.getColumnIndex(SQLiteDbHelper.RESULT_DATE));
                String name64   = c.getString(c.getColumnIndex(SQLiteDbHelper.RESULT_NAME));
                long size       = c.getLong(c.getColumnIndex(SQLiteDbHelper.RESULT_SIZE));
                long files      = c.getLong(c.getColumnIndex(SQLiteDbHelper.RESULT_FILES));
                String name = new String(Base64.decode(name64.getBytes(), Base64.DEFAULT));
                results.add(new Result(infohash,date,name,size,files));
            }while (c.moveToNext());
        }
        c.close();
        db.close();
        return results;
    }
    public List<Result> loadAll(){
        List<Result> results = null;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.query(SQLiteDbHelper.RESULT_TABLE_NAME,
                new String[]{
                        SQLiteDbHelper.RESULT_INFOHASH,
                        SQLiteDbHelper.RESULT_DATE,
                        SQLiteDbHelper.RESULT_NAME,
                        SQLiteDbHelper.RESULT_SIZE,
                        SQLiteDbHelper.RESULT_FILES
                },"",new String[]{},"","","");
        if(c.moveToFirst()){
            results = new ArrayList<>();
            do{
                String infohash = c.getString(c.getColumnIndex(SQLiteDbHelper.RESULT_INFOHASH));
                String date     = c.getString(c.getColumnIndex(SQLiteDbHelper.RESULT_DATE));
                String name     = c.getString(c.getColumnIndex(SQLiteDbHelper.RESULT_NAME));
                long size       = c.getLong(c.getColumnIndex(SQLiteDbHelper.RESULT_SIZE));
                long files      = c.getLong(c.getColumnIndex(SQLiteDbHelper.RESULT_FILES));
                results.add(new Result(infohash,date,name,size,files));
            }while (c.moveToNext());
        }
        c.close();
        db.close();
        return results;
    }
    public void clearTable(){
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("delete from " + SQLiteDbHelper.RESULT_TABLE_NAME);
        db.execSQL("update sqlite_sequence set seq = 0 where name = " + SQLiteDbHelper.RESULT_TABLE_NAME);
        db.close();
    }

    public boolean set(Result r) {
        if(exist(r.id)){
            delete(r.id);
            return false;
        }else{
            add(r);
            return true;
        }
    }
}