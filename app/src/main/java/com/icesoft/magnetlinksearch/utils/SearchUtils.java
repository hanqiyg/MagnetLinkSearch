package com.icesoft.magnetlinksearch.utils;

import android.content.Context;
import android.util.Log;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.icesoft.magnetlinksearch.dos.Query;

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
/*        ElasticRestClient.post(context,json,new TextHttpResponseHandler(){
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
        });*/
    }
}
