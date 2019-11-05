package com.icesoft.magnetlinksearch.utils;

import com.icesoft.magnetlinksearch.models.Result;
import com.icesoft.magnetlinksearch.models.ResultWithFiles;
import com.icesoft.magnetlinksearch.models.TFile;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static List<Result> JSON2Result(JSONObject response){
        try {
            List<Result> results = new ArrayList<>();
            JSONObject hits = response.getJSONObject("hits");
            JSONArray array = hits.getJSONArray("hits");
            for(int i=0;i<array.length();i++){
                JSONObject o = (JSONObject) array.get(i);
                String id = o.getString("_id");
                JSONObject highlight = o.getJSONObject("highlight");
                JSONArray names = highlight.getJSONArray("name");
                String name = "";
                for(int j=0;j<names.length();j++){
                    name = name + names.get(j);
                }
                JSONObject source = o.getJSONObject("_source");
                String date = source.getString("timestamp");
                long size = source.getLong("total_size");
                long count = source.getLong("file_count");
                results.add(new Result(id,date,name,size,count));
            }
            return results;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ResultWithFiles JSON2ResultWithFiles(JSONObject response){
        try {
            JSONObject hits = response.getJSONObject("hits");
            JSONArray array = hits.getJSONArray("hits");
            if (array.length()>0){
                JSONObject o = (JSONObject) array.get(0);
                String id = o.getString("_id");
                JSONObject source = o.getJSONObject("_source");
                String name = source.getString("name");
                String date = source.getString("timestamp");
                long size = source.getLong("total_size");
                long count = source.getLong("file_count");
                JSONArray fs = source.getJSONArray("files");
                int len = fs.length();
                List<TFile> files = new ArrayList<>();
                for(int j=0;j<len;j++){
                    JSONObject f = fs.getJSONObject(j);
                    files.add(new TFile(f.getString("name"),f.getLong("length")));
                }
                return new ResultWithFiles(id,date,name,size,count,files);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String JSON2ScrollId(JSONObject response){
        try {
            return response.getString("_scroll_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int JSON2TotalCount(JSONObject response) {
        try {
            JSONObject hits = response.getJSONObject("hits");
            JSONObject total = hits.getJSONObject("total");
            int value = total.getInt("value");
            return value;
        } catch (JSONException e) {
            e.printStackTrace();
            return -1;
        }
    }
    public static boolean JSON2Error(JSONObject response) {
        System.out.println(response.toString());
        try {
            JSONObject error = response.getJSONObject("error");
            if(error!=null){System.out.println(error.toString());}
            return error!=null;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }
}
