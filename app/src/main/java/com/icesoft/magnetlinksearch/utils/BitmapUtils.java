package com.icesoft.magnetlinksearch.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.RelativeLayout;
import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class BitmapUtils {
    public static Bitmap getScrollViewBitmap(RelativeLayout relativeLayout,int windowWidth) {
        int h = 0;
        Bitmap bitmap;
        h = relativeLayout.getHeight();
        bitmap = Bitmap.createBitmap(windowWidth, h, Bitmap.Config.ARGB_4444);
        final Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.parseColor("#f2f7fa"));
        relativeLayout.draw(canvas);
        return bitmap;
    }
    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static String saveImageToGallery(Context context, Bitmap bmp) {
        File path = context.getExternalFilesDir("test");
        Log.d("t",path.toString());
        String fileName = "Screen_" + System.currentTimeMillis() + ".jpg";
        File file = new File(path, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }
}
