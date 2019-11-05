package com.icesoft.magnetlinksearch.utils;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class FileUtils {
    public static boolean writeObject(Context context,String filename,Object o){
        FileOutputStream fos;
        ObjectOutputStream oos=null;
        try{
            fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(o);
            oos.close();
            return true;
        }catch(Exception e){
            Log.e("writeObject", "Cant write file [" + filename + "] cause by " +e.getMessage());
            e.printStackTrace();
            return false;
        }
        finally{
            if(oos!=null)
                try{
                    oos.close();
                }catch(Exception e){
                    Log.e("writeObject", "Error while closing stream "+e.getMessage());
                }
        }
    }

    public static Object readObject(Context context,String filename){
        FileInputStream fin;
        ObjectInputStream ois=null;
        try{
            fin = context.openFileInput(filename);
            ois = new ObjectInputStream(fin);
            Object o = ois.readObject();
            ois.close();
            Log.v("readObject", "Records read successfully");
            return o;
        }catch(Exception e){
            Log.e("readObject", "Cant read saved records"+e.getMessage());
            return null;
        }
        finally{
            if(ois!=null)
                try{
                    ois.close();
                }catch(Exception e){
                    Log.e("readObject", "Error in closing stream while reading records"+e.getMessage());
                }
        }
    }
}
