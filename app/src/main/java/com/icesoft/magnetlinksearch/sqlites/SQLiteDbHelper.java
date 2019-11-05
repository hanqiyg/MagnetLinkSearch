package com.icesoft.magnetlinksearch.sqlites;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteDbHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "database.db";
    public static final int DB_VERSION = 1;

    /*
    public String id;
    public String date;
    public String name;
    public long size;
    public long files;
*/
    public static final String RESULT_TABLE_NAME = "results";
    public static final String RESULT_ID = "id";
    public static final String RESULT_INFOHASH = "infohash";
    public static final String RESULT_DATE = "date";
    public static final String RESULT_NAME = "name";
    public static final String RESULT_SIZE = "size";
    public static final String RESULT_FILES = "files";

    //创建 students 表的 sql 语句
    private static final String CREATE_RESULT_TABLE_SQL =
            "create table " + RESULT_TABLE_NAME
                + "("
                    + "`" + RESULT_ID         + "`" + " integer primary key autoincrement,"
                    + "`" + RESULT_INFOHASH   + "`" + " varchar(32) not null,"
                    + "`" + RESULT_DATE       + "`" + " varchar(32) not null,"
                    + "`" + RESULT_NAME       + "`" + " varchar(512) not null,"
                    + "`" + RESULT_SIZE       + "`" + " long not null,"
                    + "`" + RESULT_FILES      + "`" + " integer not null"
                + ");";
/*
*   this.queryString = queryString;
    this.from = from;
    this.size = size;
    this.waitTime = waitTime;
    this.scrollId = scrollId;
*/
/*    public static final String QUERY_TABLE_NAME = "fragment";
    public static final String QUERY_ID = "id";
    public static final String QUERY_STRING = "qstring";
    public static final String QUERY_FROM = "start";
    public static final String QUERY_SIZE = "size";
    public static final String QUERY_TIME = "time";
    public static final String QUERY_SCROLL_ID = "sid";

    private static final String CREATE_QUERY_TABLE_SQL =
            "create table " + QUERY_TABLE_NAME
                + "("
                    + QUERY_ID          + " varchar(32) primary key,"
                    + QUERY_STRING      + " varchar(32) not null,"
                    + QUERY_FROM        + " integer not null,"
                    + QUERY_SIZE        + " integer not null,"
                    + QUERY_TIME        + " varchar(8) not null,"
                    + QUERY_SCROLL_ID   + " varchar(32)"
                + ");";*/

    public SQLiteDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_RESULT_TABLE_SQL);
        //db.execSQL(CREATE_QUERY_TABLE_SQL);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + RESULT_TABLE_NAME);
        //db.execSQL("DROP TABLE IF EXISTS " + QUERY_TABLE_NAME);
        onCreate(db);
    }
}