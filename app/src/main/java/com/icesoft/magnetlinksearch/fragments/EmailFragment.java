package com.icesoft.magnetlinksearch.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.OnClick;
import com.icesoft.magnetlinksearch.R;
import com.icesoft.magnetlinksearch.dialogs.WaitDialogFragment;
import com.icesoft.magnetlinksearch.mail.SendMailUtils;
import com.icesoft.magnetlinksearch.tasks.SendMailAsyncTask;
import com.icesoft.magnetlinksearch.utils.StringUtils;


public class EmailFragment extends BaseFragment {
    public static final String FRAGMENT_TAG = "EmailFragment";
    private String msgType;
    @BindView(R.id.content)
    EditText content;
    @BindView(R.id.address)
    EditText fromAddress;
    @BindView(R.id.select)
    Spinner type;
    SendMailAsyncTask sendMailAsyncTask;
    WaitDialogFragment dialogFragment;
    @OnClick(R.id.submit)
    public void submit(){
        String message = StringUtils.checkContent(content.getText().toString());
        String email = StringUtils.checkEmail(fromAddress.getText().toString());
        if(message != null && email != null){
            sendMailAsyncTask = new SendMailAsyncTask((AppCompatActivity) mActivity);
            sendMailAsyncTask.execute(msgType,String.format("%s\r\n\r\n%s",message,email));
        }
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
        if(sendMailAsyncTask != null){
            sendMailAsyncTask.cancel(true);
        }
    }


}
