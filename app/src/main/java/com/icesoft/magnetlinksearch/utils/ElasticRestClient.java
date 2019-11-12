package com.icesoft.magnetlinksearch.utils;
import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.auth.AuthScope;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import java.io.UnsupportedEncodingException;

public class ElasticRestClient {
    private static final String USERNAME = "elastic";
    private static final String PASSWORD = "xAH4WxXXiEsaZQQsAz0s";
    private static final String BASE_URL = "http://es.makebot.club:9200/"; //http://localhost:9200/
    private static final String CLASS_NAME = ElasticRestClient.class.getSimpleName();
    private static AsyncHttpClient client = new AsyncHttpClient();
    static{

    }


    public static void mGet(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(url, params, responseHandler);
    }
    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        String host = getAbsoluteUrl(url);
        client.addHeader(
                "Authorization",
                "Basic " + Base64.encodeToString(
                        (USERNAME+":"+PASSWORD).getBytes(),Base64.NO_WRAP)
        );
        client.get(host, params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

    public static void post(Context context, String url, String jsonObject, JsonHttpResponseHandler responseHandler){
       ByteArrayEntity entity = null;
       try {
            entity = new ByteArrayEntity(jsonObject.getBytes("UTF-8"));
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
           client.post(context,getAbsoluteUrl(url), entity, "application/json", responseHandler);
       } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
       }
    }
}
