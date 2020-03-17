package com.icesoft.magnetlinksearch.utils;

import android.content.Context;
import android.util.Base64;
import com.loopj.android.http.*;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

import java.io.UnsupportedEncodingException;

public class ElasticRestClient {

    private static final String BASE_URL = "http://es.magnet-links.com:9200/"; //http://localhost:9200/
    private static final String AUTHORIZATION = "Basic cXVlcmllcjpRdWVyaWVyMTIz";
    private static AsyncHttpClient client = new AsyncHttpClient();
    static{
        client.addHeader(
                "Authorization",
                AUTHORIZATION
        );
    }
    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
         client.get(getAbsoluteUrl(url), params, responseHandler);
    }
    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
    public static void post(Context context, String path,String jsonString, TextHttpResponseHandler responseHandler){
       ByteArrayEntity entity = null;
       try {
            entity = new ByteArrayEntity(jsonString.getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
           client.post(context,getAbsoluteUrl(path), entity, "application/json", responseHandler);
       } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
       }
    }
}
