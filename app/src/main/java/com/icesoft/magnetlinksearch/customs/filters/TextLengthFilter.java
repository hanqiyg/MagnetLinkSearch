package com.icesoft.magnetlinksearch.customs.filters;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.icesoft.magnetlinksearch.R;

public class TextLengthFilter implements InputFilter {
    private static final String T = TextLengthFilter.class.getSimpleName();
    private Context context;
    private View rootView;
    private TextView message;
    private View submit;
    private int min_length = 3;
    private int max_length = 10;
    public TextLengthFilter(Context context,View rootView, TextView message,View submit, int min, int max){
        this.context = context;
        this.rootView = rootView;
        this.message = message;
        this.submit = submit;
        this.min_length = min;
        this.max_length = max;
    }
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        int keep = max_length - (dest.length() - (dend - dstart));
        //Log.d(T,source + ",[" + start +"," + end + "] " + dest.toString() + ",[" + dstart +"," + dend +"]" + "keep=" + keep);
        if(keep < (end - start)){
            //Log.d(T,context.getString(R.string.input_less,max_length));
            message.setText(context.getString(R.string.input_less,max_length));
            rootView.setVisibility(View.VISIBLE);
        }else if((dest.length() - (dend - dstart)) + source.length() < min_length){
            //Log.d(T,context.getString(R.string.input_more,min_length));
            message.setText(context.getString(R.string.input_more,min_length));
            rootView.setVisibility(View.VISIBLE);
            submit.setEnabled(false);
        }else{
            rootView.setVisibility(View.GONE);
            submit.setEnabled(true);
        }
        if(keep <= 0){
            return "";
        }else if(keep >= end - start){
            return null;
        }else{
            return source.subSequence(start,start + keep);
        }
    }
}
