package com.icesoft.magnetlinksearch.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;
import com.icesoft.magnetlinksearch.R;
import com.icesoft.magnetlinksearch.models.Magnet;

import java.util.ArrayList;

public class EmailUtils {
    private static final String LF = System.getProperty("line.separator");
    public static void email(Context context, String[] tos,String[] ccs,String[] bcs,
            String subject,String body, ArrayList<Uri> uris,String type,String chooser){
        Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        if(tos != null)     intent.putExtra(Intent.EXTRA_EMAIL, tos);
        if(ccs != null)     intent.putExtra(Intent.EXTRA_CC, ccs);
        if(bcs != null)     intent.putExtra(Intent.EXTRA_BCC, bcs);
        if(body!= null)     intent.putExtra(Intent.EXTRA_TEXT, body);
        if(subject!= null)  intent.putExtra(Intent.EXTRA_SUBJECT, subject);

/*        ArrayList<Uri> imageUris = new ArrayList<>();
        imageUris.add(Uri.parse("file:///sdcard/Chrysanthemum.jpg"));
        imageUris.add(Uri.parse("file:///sdcard/Desert.jpg"));*/
        if(uris != null && uris.size()>0) intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        intent.setType(type);
        Intent.createChooser(intent, chooser);
        try{
            context.startActivity(intent);
        }catch (ActivityNotFoundException e){
            Toast.makeText(context,"No email App found.",Toast.LENGTH_SHORT).show();
        }
    }
    public static void email(Context context, Magnet magnet){
        String subject = String.format(context.getString(R.string.email_subject),magnet.getName());
        String body = getEmailBody(context,magnet);
        String chooser = context.getString(R.string.email_chooser);
        email(context,null,null,null,subject,body,null,"message/rfc822",chooser);
    }
    public static String getEmailBody(Context context,Magnet magnet){
        StringBuffer sb = new StringBuffer();
        sb.append(context.getString(R.string.email_body_title));sb.append(LF);
        sb.append(LF);
        sb.append(context.getString(R.string.email_body_url));       sb.append(FormatUtils.magnetFromId(magnet.getId()));    sb.append(LF);
        sb.append(context.getString(R.string.email_body_name));       sb.append(magnet.getName());                            sb.append(LF);
        sb.append(context.getString(R.string.email_body_size));       sb.append(FormatUtils.formatSize(magnet.getLength()));  sb.append(LF);
        sb.append(context.getString(R.string.email_body_timestamp));       sb.append(magnet.getTimestamp());                       sb.append(LF);
        sb.append(context.getString(R.string.email_body_count));      sb.append(String.valueOf(magnet.getCount()));           sb.append(LF);
/*        for(MFile f : magnet.getFiles()){
            sb.append(f.getName());
            sb.append("\t");
            sb.append(FormatUtils.formatSize(f.getLength()));
        }*/
        return sb.toString();
    }
}
