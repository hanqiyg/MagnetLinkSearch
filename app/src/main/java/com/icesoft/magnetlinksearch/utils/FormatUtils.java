package com.icesoft.magnetlinksearch.utils;

import android.text.Html;
import android.text.Spanned;
import com.icesoft.magnetlinksearch.models.Magnet;

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
        final int carry = 1024;
        long rest = 0;
        if(size < carry){
            return String.valueOf(size) + "B";
        }else{
            size /= carry;
        }

        if(size < carry){
            return String.valueOf(size) + "KB";
        }else{
            rest = size % carry;
            size /= carry;
        }

        if(size < carry){
            size = size * 100;
            return String.valueOf((size / 100)) + "." + String.valueOf((rest * 100 / carry % 100)) + "MB";
        }else{
            size = size * 100 / carry;
            return String.valueOf((size / 100)) + "." + String.valueOf((size % 100)) + "GB";
        }
    }
    public static String formatNum(long size){
        final int carry = 1000;
        long rest = 0;
        if(size < carry){
            return String.valueOf(size) + "";
        }else{
            size /= carry;
        }

        if(size < carry){
            return String.valueOf(size) + "K";
        }else{
            rest = size % carry;
            size /= carry;
        }

        if(size < carry){
            size = size * 100;
            return String.valueOf((size / 100)) + "." + String.valueOf((rest * 100 / carry % 100)) + "M";
        }else{
            size = size * 100 / carry;
            return String.valueOf((size / 100)) + "." + String.valueOf((size % 100)) + "G";
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
    public static String shareText(Magnet r) {
        StringBuffer sb = new StringBuffer();
        sb.append("magnet: ").append(magnetFromId(r.getId())).append("\n\r");
        sb.append("name:   ").append(r.getName()).append("\n\r");
        sb.append("size:   ").append(formatSize(r.getLength())).append("\n\r");
        sb.append("count:  ").append(r.getCount()).append("\n\r");
        sb.append("update: ").append(r.getTimestamp()).append("\n\r");
        sb.append(SHARED_BY ).append("\n\r");
        sb.append(COPYRIGHT_BY).append("\n\r");
        return sb.toString();
    }
}
