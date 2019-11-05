package com.icesoft.magnetlinksearch.models;

import android.widget.RelativeLayout;

import java.util.List;

public class ResultWithFiles extends Result {
    public List<TFile> files = null;
    public ResultWithFiles(String id, String date, String name, long size, long filecount) {
        super(id, date, name, size, filecount);
    }
    public ResultWithFiles(String id, String date, String name, long size, long filecount, List<TFile> files) {
        super(id, date, name, size, filecount);
        this.files = files;
    }
}
