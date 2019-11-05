package com.icesoft.magnetlinksearch.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.icesoft.magnetlinksearch.Constance;

public class SharedPreferencesUtils {
    public static boolean writeDocumentId(Context context, String id) {
        if(context != null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(Constance.PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(Constance.PREFERENCE_KEY_INFOHASH, id);
            return editor.commit();
        }
        return false;
    }
    public static String readDocumentId(Context context) {
        if(context != null){
            SharedPreferences sharedPreferences = context.getSharedPreferences(Constance.PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
            return sharedPreferences.getString(Constance.PREFERENCE_KEY_INFOHASH,null);
        }
        return null;
    }
    public static boolean writeQuery(Context context, String q,int from) {
        if(context != null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(Constance.PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(Constance.KEY_SIMPLE_QUERY, q);
            editor.putLong(Constance.KEY_SIMPLE_FROM, from);
            editor.putLong(Constance.KEY_SIMPLE_SIZE, Constance.QUERY_SIZE);
            editor.putLong(Constance.KEY_SIMPLE_FROM, 0);
            return editor.commit();
        }
        return false;
    }
}
