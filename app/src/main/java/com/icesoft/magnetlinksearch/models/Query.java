package com.icesoft.magnetlinksearch.models;

import java.io.Serializable;
import java.util.List;

public class Query implements Serializable {
    public String context;
    public int from;
    public int size;
    public int total;
    public List<Result> results;

    public Query(String context, int from, int size, int total, List<Result> results) {
        this.context = context;
        this.from = from;
        this.size = size;
        this.total = total;
        this.results = results;
    }
    public Query(){}
}
