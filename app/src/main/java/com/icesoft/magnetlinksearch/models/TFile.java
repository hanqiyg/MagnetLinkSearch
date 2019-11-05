package com.icesoft.magnetlinksearch.models;

public class TFile {
    public String name;
    public long length;

    public TFile(String name, long length) {
        this.name = name;
        this.length = length;
    }
    public TFile() {
    }
}