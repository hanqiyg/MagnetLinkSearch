package com.icesoft.magnetlinksearch.mappers;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Set;
public class Source {
    @JsonProperty(value = "name")
    String name;
    @JsonProperty(value = "total_size")
    long length;
    @JsonProperty(value = "file_count")
    int count;
    @JsonProperty(value = "files")
    List<MFile> mFiles;
    @JsonProperty(value = "timestamp")
    String timestamp;

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

    public List<MFile> getmFiles() {
        return mFiles;
    }

    public void setmFiles(List<MFile> mFiles) {
        this.mFiles = mFiles;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
