package com.icesoft.magnetlinksearch.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.icesoft.magnetlinksearch.App;
import com.icesoft.magnetlinksearch.mappers.MFile;

import java.io.IOException;
import java.util.List;

public class JsonUtils {
    public static String FilesToJsonString(List<MFile> files){
        String json = null;
        if(files!= null && files.size()>0){
            try{
                json = App.getApp().getMapper().writeValueAsString(files);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return json;
    }
    public static List<MFile> JsonStringToFiles(String jsonString){
        List<MFile> files = null;
        if(jsonString != null){
            try{
                files = App.getApp().getMapper().readValue(jsonString,new TypeReference<List<MFile>>() {});
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return files;
    }
}
