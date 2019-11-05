package com.icesoft.magnetlinksearch.models;

import java.io.Serializable;

public class Result implements Serializable {
    public String id;
    public String date;
    public String name;
    public long size;
    public long count;

    public Result(String id, String date, String name, long size, long count) {
        this.id = id;
        this.date = date;
        this.name = name;
        this.size = size;
        this.count = count;
    }

    @Override
    public String toString() {
        return "SearchResult{" +
                "id='" + id + '\'' +
                ", date='" + date + '\'' +
                ", name='" + name + '\'' +
                ", size=" + size +
                ", filecount=" + count +
                '}';
    }
}
