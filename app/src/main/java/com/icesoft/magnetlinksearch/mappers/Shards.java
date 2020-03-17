package com.icesoft.magnetlinksearch.mappers;

import com.fasterxml.jackson.annotation.JsonProperty;

/*
* "total" : 1,
"successful" : 1,
"skipped" : 0,
"failed" : 0
* */
public class Shards {
    @JsonProperty(value = "total")
    long total;
    @JsonProperty(value = "successful")
    long successful;
    @JsonProperty(value = "skipped")
    long skipped;
    @JsonProperty(value = "failed")
    long failed;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getSuccessful() {
        return successful;
    }

    public void setSuccessful(long successful) {
        this.successful = successful;
    }

    public long getSkipped() {
        return skipped;
    }

    public void setSkipped(long skipped) {
        this.skipped = skipped;
    }

    public long getFailed() {
        return failed;
    }

    public void setFailed(long failed) {
        this.failed = failed;
    }
}
