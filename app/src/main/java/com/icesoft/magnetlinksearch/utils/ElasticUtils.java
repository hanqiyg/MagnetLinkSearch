package com.icesoft.magnetlinksearch.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.ScrollView;
import android.widget.TextView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.icesoft.magnetlinksearch.R;
import com.icesoft.magnetlinksearch.dos.Query;
import com.icesoft.magnetlinksearch.dos.SearchFails;
import com.icesoft.magnetlinksearch.mappers.EsCount;
import com.icesoft.magnetlinksearch.mappers.InnerHits;
import com.icesoft.magnetlinksearch.mappers.Root;
import com.icesoft.magnetlinksearch.mappers.Source;
import com.icesoft.magnetlinksearch.models.Magnet;
import com.loopj.android.http.TextHttpResponseHandler;
import com.unnamed.b.atv.view.AndroidTreeView;
import cz.msebera.android.httpclient.Header;
import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ElasticUtils {
    private static final String T = ElasticUtils.class.getSimpleName();
    private static final String PATH_COUNT = "/test/_count";
    private static final String PATH_SEARCH = "/test/_search";

    public static void getMagnetCount(final Context context,final TextView tvCount){
        ElasticRestClient.get(PATH_COUNT,null,new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                httpOnFailure("getMagnetCount",statusCode,headers,responseString,throwable);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                ObjectMapper mapper = new ObjectMapper();
                EsCount escount = null;
                try {
                    escount = mapper.readValue(responseString,EsCount.class);
                } catch (IOException e) {
                    Log.d(T,String.format(context.getResources().getString(R.string.MapperError),responseString));
                }
                if(escount!=null){
                    tvCount.setText(String.format(context.getResources().getString(R.string.total_count),
                            FormatUtils.formatNum(escount.getCount())));
                }
            }
        });
    }
    public static void getFilesById(final Activity activity, final ScrollView root, final String id){
        String json = String.format("{\"query\" : {\"match\":{\"_id\":\"%s\"}}}",id);
        ElasticRestClient.post(activity,PATH_SEARCH,json,new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                httpOnFailure("getMagnetById",statusCode,headers,responseString,throwable);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                ObjectMapper mapper = new ObjectMapper();
                Root m = null;
                try {
                    m = mapper.readValue(responseString, Root.class);
                } catch (IOException e) {
                    Log.d(T,String.format(activity.getResources().getString(R.string.MapperError),responseString));
                    e.printStackTrace();
                }
                if(m!=null){
                    AndroidTreeView tree = TreeUtils.getTreeView(activity,m.getHits().getInnerHits().get(0).getSource().getmFiles());
                    root.addView(tree.getView());
                }
            }
        });
    }
    public static void stringSearch(final Context context,String keyword, int from, int size){
        @SuppressLint("DefaultLocale")
        String json = String.format("{\"query\" : {\"query_string\":{\"query\":\"%s\"}},\"from\" : %d , \"size\" : %d}",
                keyword,from,size);
        ElasticRestClient.post(context,PATH_SEARCH,json,new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                httpOnFailure("stringSearch",statusCode,headers,responseString,throwable);
                EventBus.getDefault().postSticky(new SearchFails(statusCode,responseString));
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                ObjectMapper mapper = new ObjectMapper();
                Root m = null;
                try {
                    m = mapper.readValue(responseString, Root.class);
                } catch (IOException e) {
                    Log.d(T,String.format(context.getResources().getString(R.string.MapperError),responseString));
                    e.printStackTrace();
                }
                if(m!=null){
                    List<Magnet> magnets = ResponseToMagnets(m);
                    EventBus.getDefault().postSticky(magnets);
                }
            }
        });
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

    private static void httpOnFailure(String functionName, int statusCode, Header[] headers,
                                      String responseString, Throwable throwable){
        StringBuffer sb = new StringBuffer();
        sb.append("statusCode:[");
        sb.append(String.valueOf(statusCode));
        sb.append("] Response: [");
        sb.append(responseString);
        sb.append("]");
        Log.d(functionName,sb.toString());
    }
}
