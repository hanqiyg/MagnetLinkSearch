package com.icesoft.magnetlinksearch.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import butterknife.BindView;
import butterknife.OnClick;
import com.icesoft.magnetlinksearch.R;
import com.icesoft.magnetlinksearch.dialogs.WaitDialogFragment;
import com.icesoft.magnetlinksearch.events.MailEvent;
import com.icesoft.magnetlinksearch.mail.SendMailIntentService;
import com.icesoft.magnetlinksearch.utils.StringUtils;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class EmailFragment extends BaseFragment {
    public static final String FRAGMENT_TAG = "EmailFragment";
    private String msgType;
    @BindView(R.id.content)
    EditText content;
    @BindView(R.id.address)
    EditText fromAddress;
    @BindView(R.id.select)
    Spinner type;

    @BindView(R.id.submit)
    Button submit;
    @OnClick(R.id.submit)
    public void submit(){
        Log.d(FRAGMENT_TAG,"submit");
        String message = StringUtils.checkContent(content.getText().toString());
        String email = StringUtils.checkEmail(fromAddress.getText().toString());
        if(message != null && email != null){
            Log.d(FRAGMENT_TAG,message + email);
            Intent intent = new Intent(context, SendMailIntentService.class);
            intent.putExtra(SendMailIntentService.TITLE, msgType);
            intent.putExtra(SendMailIntentService.CONTENT, String.format("%s \r\n %s",message,email));
            context.startService(intent);
        }else{
            Log.d(FRAGMENT_TAG,"error");
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSuccess(MailEvent event){
        Log.d(FRAGMENT_TAG,"SendMail: " + (event.success?"success":"failure"));
    }

    @Override
    int getLayoutResourceID() {
        return R.layout.fragment_email;
    }

    @Override
    String getName() {
        return FRAGMENT_TAG;
    }

    @Override
    void initView() {
        msgType = content.getResources().getStringArray(R.array.type)[0];
        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] array = content.getResources().getStringArray(R.array.type);
                if(position<array.length){
                    msgType = array[position];
                }else{
                    msgType = array[0];
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                msgType = content.getResources().getStringArray(R.array.type)[0];
            }
        });
    }

    @Override
    void initData() {

    }

    @Override
    protected void refreshData() {

    }
    public static EmailFragment newInstance(String text){
        EmailFragment fragment = new EmailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY,text);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    String getBackStack() {
        return SearchFragment.FRAGMENT_TAG;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    @Override
    public void online() { submit.setEnabled(true); }
    @Override
    public void offline() {submit.setEnabled(false);}
}
