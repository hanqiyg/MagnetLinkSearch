package com.icesoft.magnetlinksearch.sqlites;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteDbHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "database.db";
    public static final int DB_VERSION = 1;

    public static final String MAGNET_TABLE_NAME = "magnets";
    public static final String MAGNET_AUTOINCR = "id";
    public static final String MAGNET_ID = "infohash";
    public static final String MAGNET_TIMESTAMP = "timestamp";
    public static final String MAGNET_NAME = "name";
    public static final String MAGNET_FILESIZE = "filesize";
    public static final String MAGNET_FILECOUNT = "filecount";
    public static final String MAGNET_FILEJSON = "filejson";


    private static final String CREATE_MAGNET_TABLE_SQL =
            "create table " + MAGNET_TABLE_NAME
                + "("
                    + "`" + MAGNET_AUTOINCR   + "`" + " integer primary key autoincrement,"
                    + "`" + MAGNET_ID         + "`" + " varchar not null,"
                    + "`" + MAGNET_TIMESTAMP  + "`" + " varchar not null,"
                    + "`" + MAGNET_NAME       + "`" + " varchar not null,"
                    + "`" + MAGNET_FILESIZE   + "`" + " long not null,"
                    + "`" + MAGNET_FILECOUNT  + "`" + " integer not null,"
                    + "`" + MAGNET_FILEJSON   + "`" + " varchar not null"
                + ");";


    public SQLiteDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_MAGNET_TABLE_SQL);
        //db.execSQL(CREATE_QUERY_TABLE_SQL);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MAGNET_TABLE_NAME);
        onCreate(db);
    }
}