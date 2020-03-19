package com.icesoft.magnetlinksearch.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.widget.ScrollView;
import android.widget.Toast;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.icesoft.magnetlinksearch.events.FileTreeEvent;
import com.icesoft.magnetlinksearch.events.MagnetCountEvent;
import com.icesoft.magnetlinksearch.events.SearchFailEvent;
import com.icesoft.magnetlinksearch.events.SearchSuccessEvent;
import com.icesoft.magnetlinksearch.mappers.*;
import com.icesoft.magnetlinksearch.models.Magnet;
import com.loopj.android.http.TextHttpResponseHandler;
import cz.msebera.android.httpclient.Header;
import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ElasticUtils {
    private static final String T = ElasticUtils.class.getSimpleName();
    private static final String PATH_COUNT = "/test/_count";
    private static final String PATH_SEARCH = "/test/_search";

    public static void getMagnetCount(Context context){
        ElasticRestClient.get(PATH_COUNT,null,new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                httpOnFailure("getMagnetCount",context,statusCode,headers,responseString,throwable);
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                httpOnSuccess("getMagnetCount",context,statusCode,headers,responseString);
                ObjectMapper mapper = new ObjectMapper();
                EsCount escount = null;
                try {
                    escount = mapper.readValue(responseString,EsCount.class);
                } catch (IOException e) {
                    jsonOnException("getMagnetCount",context,e);
                }
                if(escount!=null){
                    EventBus.getDefault().postSticky(new MagnetCountEvent(escount.getCount()));
                }
            }
        });
    }
    public static void getFilesById(final Context context, final String id){
        String json = String.format("{\"query\" : {\"match\":{\"_id\":\"%s\"}}}",id);
        ElasticRestClient.post(context,PATH_SEARCH,json,new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                httpOnFailure("getMagnetById",context,statusCode,headers,responseString,throwable);
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                httpOnSuccess("getFilesById",context,statusCode,headers,responseString);
                ObjectMapper mapper = new ObjectMapper();
                Root m = null;
                try {
                    m = mapper.readValue(responseString, Root.class);
                } catch (IOException e) {
                    jsonOnException("getFilesById",context,e);
                }
                if(m!=null){
                    EventBus.getDefault().postSticky(new FileTreeEvent(getFileFromRoot(m)));
                }
            }
        });
    }
    private static List<MFile> getFileFromRoot(Root r){
        if(r!=null){
            if(r.getHits()!=null){
                if(r.getHits().getInnerHits()!=null && r.getHits().getInnerHits().size()>0){
                    if(r.getHits().getInnerHits().get(0)!=null){
                        if(r.getHits().getInnerHits().get(0).getSource()!=null){
                            if(r.getHits().getInnerHits().get(0).getSource().getmFiles()!=null &&
                                    r.getHits().getInnerHits().get(0).getSource().getmFiles().size()>0){
                                return r.getHits().getInnerHits().get(0).getSource().getmFiles();
                            }
                        }
                    }
                }
            }
        }
        return new ArrayList<MFile>();
    }
    /*
    * {
        "_source": {
            "excludes": [ "files" ]
        },
        "query" : {
            "query_string" : { "query" : "test" }
        },
        "from" : 0,
        "size" : 10
    }
*/
    public static void stringSearch(final Context context,String keyword, int from, int size){
        Log.d(T,"stringSearch:" + keyword + "from:" + from + "size:" + size);
        @SuppressLint("DefaultLocale")
        String json = String.format("{\"_source\": {\"excludes\": [ \"files\" ]},\"query\" : {\"query_string\":{\"query\":\"%s\"}},\"from\" : %d , \"size\" : %d}",
                keyword,from,size);
        ElasticRestClient.post(context,PATH_SEARCH,json,new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                httpOnFailure("stringSearch",context,statusCode,headers,responseString,throwable);
                EventBus.getDefault().postSticky(new SearchFailEvent(statusCode,responseString));
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                httpOnSuccess("stringSearch",context,statusCode,headers,responseString);
                ObjectMapper mapper = new ObjectMapper();
                Root m = null;
                try {
                    m = mapper.readValue(responseString, Root.class);
                } catch (IOException e) {
                    jsonOnException("stringSearch",context,e);
                }
                if(m!=null){
                    int total = getTotal(m);
                    List<Magnet> magnets = ResponseToMagnets(m);
                    EventBus.getDefault().postSticky(new SearchSuccessEvent(from==0,total,magnets));
                }
            }
        });
    }
    private static int getTotal(Root root){
        int total = 0;
        if(root!=null){
            if(root.getHits()!= null){
                if(root.getHits().getTotal()!=null){
                    total = (int) root.getHits().getTotal().getValue();
                }
            }
        }
        return total;
    }
    private static List<Magnet> ResponseToMagnets(Root root){
        List<Magnet> magnets = new ArrayList<>();
        if(root != null){
            if(root.getHits() != null){
                if(root.getHits().getInnerHits() !=null && root.getHits().getInnerHits().size()>0){
                    for(InnerHits hit : root.getHits().getInnerHits()){
                        if(hit.getId() !=null && hit.getSource() !=null){
                            magnets.add(SourceToMagnet(hit.getId(),hit.getSource()));
                        }
                    }
                }
            }
        }
        return magnets;
    }
    private static Magnet SourceToMagnet(String id, Source source){
        return new Magnet(id,source.getName(),source.getLength(),source.getCount(),source.getmFiles(),source.getTimestamp());
    }

    private static void httpOnFailure(String functionName, Context context,int statusCode, Header[] headers,
                                      String responseString, Throwable throwable){
        StringBuffer sb = new StringBuffer();
        sb.append("statusCode:[");
        sb.append(String.valueOf(statusCode));
        sb.append("] Response: [");
        sb.append(responseString);
        sb.append("]");
        Toast.makeText(context,functionName + "[" + statusCode + "]",Toast.LENGTH_LONG).show();
        Log.d(T,functionName + "-" + sb.toString());
    }
    private static void httpOnSuccess(String functionName, Context context,int statusCode, Header[] headers, String responseString){
        StringBuffer sb = new StringBuffer();
        sb.append("statusCode:[");
        sb.append(String.valueOf(statusCode));
        sb.append("] Response: [");
        sb.append(responseString);
        sb.append("]");
        Toast.makeText(context,functionName + "[" + statusCode + "]",Toast.LENGTH_LONG).show();
        Log.d(T,functionName + "-" + sb.toString());
    }
    private static void jsonOnException(String functionName,Context context,IOException e){
        Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
        Log.d(T,functionName + "-" + e.getMessage());
    }
}
