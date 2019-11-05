package com.icesoft.magnetlinksearch.models;

import java.io.Serializable;
import java.util.List;

public class Favorite implements Serializable {
    public int from;
    public int limit;
    public int total;
    public List<Result> results;
    public boolean nodata = false;

    public Favorite(int from, int limit, int total, List<Result> results) {
        this.from = from;
        this.limit = limit;
        this.total = total;
        this.results = results;
    }
    public Favorite(){}
}
