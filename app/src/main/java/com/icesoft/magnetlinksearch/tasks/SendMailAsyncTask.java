package com.icesoft.magnetlinksearch.tasks;

import android.content.Context;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import com.icesoft.magnetlinksearch.dialogs.WaitDialogFragment;
import com.icesoft.magnetlinksearch.mail.SendMailUtils;

public class SendMailAsyncTask extends AsyncTask<String,Void,Boolean> {
    private AppCompatActivity context;
    private WaitDialogFragment dialogFragment;
    public SendMailAsyncTask(AppCompatActivity context){
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialogFragment = WaitDialogFragment.newInstance(null);
        dialogFragment.show(context.getSupportFragmentManager(),WaitDialogFragment.FRAGMENT_TAG);
    }
    @Override
    protected Boolean doInBackground(String... strings) {
        if(strings.length>=2){
            return SendMailUtils.send(strings[0],strings[1]);
        }
        return false;
    }
    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        dialogFragment.dismiss();
    }
    @Override
    protected void onCancelled() {
        super.onCancelled();
        dialogFragment.dismiss();
    }
}