package com.icesoft.magnetlinksearch.mappers;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Root {
    @JsonProperty(value = "took")
    long took;
    @JsonProperty(value = "timed_out")
    boolean tamedout;
    @JsonProperty(value = "_shards")
    Shards shards;
    @JsonProperty(value = "hits")
    Hits hits;

    public void setTook(long took) {
        this.took = took;
    }

    public void setTamedout(boolean tamedout) {
        this.tamedout = tamedout;
    }

    public void setShards(Shards shards) {
        this.shards = shards;
    }

    public void setHits(Hits hits) {
        this.hits = hits;
    }

    public long getTook() {
        return took;
    }

    public boolean isTamedout() {
        return tamedout;
    }

    public Shards getShards() {
        return shards;
    }

    public Hits getHits() {
        return hits;
    }
}
