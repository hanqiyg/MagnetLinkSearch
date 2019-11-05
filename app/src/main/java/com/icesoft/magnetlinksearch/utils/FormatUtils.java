package com.icesoft.magnetlinksearch.utils;

import android.text.Html;
import android.text.Spanned;
import com.icesoft.magnetlinksearch.models.Result;

public class FormatUtils {
    //2019-10-13T03:50:18.448786Z
    public static String formatDate(String context){
        String data[] = context.split("T");
        if(data.length>1){
            return data[0];
        }
        return context;
    }
    public static String formatSize(long size){
        long rest = 0;
        if(size < 1024){
            return String.valueOf(size) + "B";
        }else{
            size /= 1024;
        }

        if(size < 1024){
            return String.valueOf(size) + "KB";
        }else{
            rest = size % 1024;
            size /= 1024;
        }

        if(size < 1024){
            size = size * 100;
            return String.valueOf((size / 100)) + "." + String.valueOf((rest * 100 / 1024 % 100)) + "MB";
        }else{
            size = size * 100 / 1024;
            return String.valueOf((size / 100)) + "." + String.valueOf((size % 100)) + "GB";
        }
    }
    public static Spanned htmlText(String text){
        if(text!=null){
            return Html.fromHtml(text.replace("<em>","<font color=\"red\" >")
                    .replace("</em>","</font>"));
        }else{
            return Html.fromHtml("");
        }
    }
    public static final String MAGNET_HEAD = "magnet:?xt=urn:btih:";
    public static String magnetFromId(String id){
        return MAGNET_HEAD + id;
    }
    public static final String SHARED_BY    = "magnet search is a simple app.";
    public static final String COPYRIGHT_BY = "copyright belong to the author.";
    public static String shareText(Result r) {
        StringBuffer sb = new StringBuffer();
        sb.append("magnet: ").append(magnetFromId(r.id)).append("\n\r");
        sb.append("name:   ").append(r.name)            .append("\n\r");
        sb.append("size:   ").append(formatSize(r.size)).append("\n\r");
        sb.append("count:  ").append(r.count)           .append("\n\r");
        sb.append("update: ").append(r.date)            .append("\n\r");
        sb.append(SHARED_BY )                           .append("\n\r");
        sb.append(COPYRIGHT_BY)                         .append("\n\r");
        return sb.toString();
    }
}
