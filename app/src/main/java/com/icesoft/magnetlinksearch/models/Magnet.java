package com.icesoft.magnetlinksearch.models;

import com.icesoft.magnetlinksearch.mappers.MFile;

import java.util.List;

public class Magnet {
    private String id;
    private String name;
    private long length;
    private int count;
    private List<MFile> files;
    private String timestamp;

    public Magnet(String id,String name,long length,int count,List<MFile> files,String timestamp){
        this.id = id;
        this.name = name;
        this.length = length;
        this.count = count;
        this.files = files;
        this.timestamp = timestamp;
    }
    public Magnet(String name,long length,int count,List<MFile> files,String timestamp){
        this.name = name;
        this.length = length;
        this.count = count;
        this.files = files;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<MFile> getFiles() {
        return files;
    }

    public void setFiles(List<MFile> files) {
        this.files = files;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
