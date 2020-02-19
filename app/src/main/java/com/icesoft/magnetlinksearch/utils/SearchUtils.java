package com.icesoft.magnetlinksearch.utils;

import android.content.Context;
import android.util.Log;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.icesoft.magnetlinksearch.dos.Query;
import com.icesoft.magnetlinksearch.dos.Response;
import com.icesoft.magnetlinksearch.dos.SearchFails;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;
import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
public class SearchUtils {
    public static final String T = "SearchUtils";
    private static final int SIZE = 10;
    public static void search(Context context,String keyword, int from){
        Log.d(T,String.format("search:[%s]from%d.",keyword,from));
        ObjectMapper objectMapper = new ObjectMapper();
        String json = null;
        try {
            json = objectMapper.writeValueAsString(new Query(Query.Type.StringQuery,keyword,from,SIZE));
        } catch (JsonProcessingException e) {
            Log.d(T,"query jsonify exception.");
        }
        post(context,json,new TextHttpResponseHandler(){
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                EventBus.getDefault().postSticky(new SearchFails(statusCode,responseString));
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if(responseString != null){
                    try {
                        Response response = objectMapper.readValue(responseString, Response.class);
                        EventBus.getDefault().postSticky(response);
                    } catch (IOException e) {
                        Log.d(T,"response read exception.");
                    }
                }
            }
        });
    }

    private static final String BASE_URL = "https://c95avy0b87.execute-api.ap-northeast-1.amazonaws.com/v1";
    private static AsyncHttpClient client = new AsyncHttpClient();
    public static void post(Context context, String jsonString, TextHttpResponseHandler responseHandler){
        ByteArrayEntity entity = null;
        try {
            entity = new ByteArrayEntity(jsonString.getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            client.post(context,BASE_URL, entity, "application/json", responseHandler);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
