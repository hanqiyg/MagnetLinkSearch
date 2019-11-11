package com.icesoft.magnetlinksearch.fragments;

import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import com.icesoft.magnetlinksearch.R;
import com.icesoft.magnetlinksearch.customs.filters.TextLengthFilter;
import com.icesoft.magnetlinksearch.events.QueryEvent;
import com.icesoft.magnetlinksearch.events.ShowFragmentEvent;
import com.icesoft.magnetlinksearch.mail.SendMailUtil;
import com.icesoft.magnetlinksearch.utils.ElasticRestClient;
import com.icesoft.magnetlinksearch.utils.KeybordUtil;
import com.icesoft.magnetlinksearch.utils.Utils;
import com.loopj.android.http.JsonHttpResponseHandler;
import cz.msebera.android.httpclient.Header;
import io.noties.markwon.Markwon;
import io.noties.markwon.html.HtmlPlugin;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;


public class EmailFragment extends BaseFragment {
    public static final String FRAGMENT_TAG = "EmailFragment";
    private String msgType;
    @BindView(R.id.content)
    EditText content;
    @BindView(R.id.address)
    EditText fromAddress;
    @BindView(R.id.select)
    Spinner type;

    @OnClick(R.id.submit)
    public void submit(){
        SendMailUtil.send(msgType,getContent(content,fromAddress));
    }

    private String getContent(EditText content,EditText fromAddress) {
        String c = content.getText().toString();
        String a = fromAddress.getText().toString();
        return String.format("%s\r\n\r\n%s",c,a);
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
}
