package com.icesoft.magnetlinksearch.sqlites;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Base64;
import android.util.Log;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.icesoft.magnetlinksearch.mappers.MFile;
import com.icesoft.magnetlinksearch.models.Magnet;
import com.icesoft.magnetlinksearch.utils.JsonUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MagnetDAO {
    private static final String TAG = "MagnetDAO";
    SQLiteDbHelper helper = null;
    public static final String QUEST = " = ? ";
    public MagnetDAO(Context context){
        helper = new SQLiteDbHelper(context);
    }
/*    public void addAll(List<Magnet> magnets) {
        SQLiteDatabase db = helper.getWritableDatabase();
        Magnet m = null;
        db.beginTransaction();
        int len = magnets.size();
        ObjectMapper map = new ObjectMapper();
        for (int i = 0; i < len; i++) {
            m = magnets.get(i);
            String filejson = "";
            try{
                filejson = map.writeValueAsString(m.getFiles());
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            db.execSQL("INSERT INTO "+ SQLiteDbHelper.MAGNET_TABLE_NAME +
                    " (" +
                        "`" + SQLiteDbHelper.MAGNET_ID          + "`," +
                        "`" + SQLiteDbHelper.MAGNET_TIMESTAMP   + "`," +
                        "`" + SQLiteDbHelper.MAGNET_NAME        + "`," +
                        "`" + SQLiteDbHelper.MAGNET_FILESIZE    + "`," +
                        "`" + SQLiteDbHelper.MAGNET_FILECOUNT   + "`," +
                        "`" + SQLiteDbHelper.MAGNET_FILEJSON    + "`" +
                    ") VALUES " +
                    "(" +
                        "'" + m.getId()         + "'," +
                        "'" + m.getTimestamp()  + "'," +
                        "'" + m.getName()       + "'," +
                        "'" + m.getLength()     + "'," +
                        "'" + m.getCount()      + "'," +
                        "'" + filejson          + "'" +
                    ")");
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }*/
    public void add(Magnet m) {
        if(m !=null){
            String name64 = m.getName()!=null?Base64.encodeToString(m.getName().getBytes(), Base64.DEFAULT):"";
            SQLiteDatabase db = helper.getWritableDatabase();
            String filejson = JsonUtils.FilesToJsonString(m.getFiles());
            db.beginTransaction();
            db.execSQL("INSERT INTO "+ SQLiteDbHelper.MAGNET_TABLE_NAME +
                    " (" +
                    "`" + SQLiteDbHelper.MAGNET_ID          + "`," +
                    "`" + SQLiteDbHelper.MAGNET_TIMESTAMP   + "`," +
                    "`" + SQLiteDbHelper.MAGNET_NAME        + "`," +
                    "`" + SQLiteDbHelper.MAGNET_FILESIZE    + "`," +
                    "`" + SQLiteDbHelper.MAGNET_FILECOUNT   + "`," +
                    "`" + SQLiteDbHelper.MAGNET_FILEJSON    + "`" +
                    ") VALUES " +
                    "(" +
                    "'" + m.getId()         + "'," +
                    "'" + m.getTimestamp()  + "'," +
                    "'" + name64            + "'," +
                    "'" + m.getLength()     + "'," +
                    "'" + m.getCount()      + "'," +
                    "'" + filejson          + "'" +
                    ")");
            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();
        }
    }
    public boolean exist(String id){
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.query(SQLiteDbHelper.MAGNET_TABLE_NAME,
                new String[]{
                        SQLiteDbHelper.MAGNET_ID
                },SQLiteDbHelper.MAGNET_ID + " = ? ",new String[]{id},"","","");
        boolean isExist = c.moveToFirst();
        c.close();
        db.close();
        Log.d(TAG,(isExist?"DoExist:":"NotExist:") + id);
        return isExist;
    }
    public boolean delete(String id){
        SQLiteDatabase db = helper.getWritableDatabase();
        int r = db.delete(SQLiteDbHelper.MAGNET_TABLE_NAME, SQLiteDbHelper.MAGNET_ID + QUEST, new String[]{id});
        db.close();
        return r==1;
    }
    public int count(){
        int size = 0;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.query(SQLiteDbHelper.MAGNET_TABLE_NAME,
                new String[]{"count(id)"},
                "",new String[]{},"","","");
        if(c.moveToFirst()){
            size = c.getInt(0);
        }
        c.close();
        db.close();
        return size;
    }
    public List<Magnet> load(int from,int limit){
        List<Magnet> magnets = null;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.query(SQLiteDbHelper.MAGNET_TABLE_NAME,
                new String[]{
                        SQLiteDbHelper.MAGNET_ID,
                        SQLiteDbHelper.MAGNET_TIMESTAMP,
                        SQLiteDbHelper.MAGNET_NAME,
                        SQLiteDbHelper.MAGNET_FILESIZE,
                        SQLiteDbHelper.MAGNET_FILECOUNT,
                        SQLiteDbHelper.MAGNET_FILEJSON
                },"",new String[]{},"","","",
                 from + "," + limit);
        ObjectMapper map = new ObjectMapper();
        if(c.moveToFirst()){
            magnets = new ArrayList<>();
            do{
                String id       = c.getString(c.getColumnIndex(SQLiteDbHelper.MAGNET_ID));
                String timestamp= c.getString(c.getColumnIndex(SQLiteDbHelper.MAGNET_TIMESTAMP));
                String name64   = c.getString(c.getColumnIndex(SQLiteDbHelper.MAGNET_NAME));
                long length     = c.getLong(c.getColumnIndex(SQLiteDbHelper.MAGNET_FILESIZE));
                int count       = c.getInt(c.getColumnIndex(SQLiteDbHelper.MAGNET_FILECOUNT));
                String json     = c.getString(c.getColumnIndex(SQLiteDbHelper.MAGNET_FILEJSON));
                String name = new String(Base64.decode(name64.getBytes(), Base64.DEFAULT));
                List<MFile> files = JsonUtils.JsonStringToFiles(json);
                magnets.add(new Magnet(id,name,length,count,files,timestamp));
            }while (c.moveToNext());
        }
        c.close();
        db.close();
        return magnets;
    }

    public void clearTable(){
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("delete from " + SQLiteDbHelper.MAGNET_TABLE_NAME);
        db.execSQL("update sqlite_sequence set seq = 0 where name = " + SQLiteDbHelper.MAGNET_TABLE_NAME);
        db.close();
    }

    public boolean set(Magnet m) {
        String id = m.getId();
        if(exist(id)){
            delete(id);
            return false;
        }else{
            add(m);
            return true;
        }
    }
}