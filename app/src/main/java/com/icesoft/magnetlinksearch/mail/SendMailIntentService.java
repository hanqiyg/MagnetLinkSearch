package com.icesoft.magnetlinksearch.mail;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import com.icesoft.magnetlinksearch.events.MailEvent;
import org.greenrobot.eventbus.EventBus;

public class SendMailIntentService extends IntentService {
    private static final String T = "SendMailIntentService";
    public static final String TITLE = "title";
    public static final String CONTENT = "content";
    public SendMailIntentService() {
        super(T);
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        MailEvent event = new MailEvent();
        event.success = false;
        event.enable = false;
        EventBus.getDefault().post(event);
        String title = intent.getStringExtra(TITLE);
        String content = intent.getStringExtra(CONTENT);
        Log.d(T,title+content);
        boolean success = SendMailUtils.send(title,content);
        EventBus.getDefault().post(event);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(T,"onCreate");
    }

    @Override
    public void onDestroy() {
        Log.d(T,"onDestroy");
        super.onDestroy();
    }
}
