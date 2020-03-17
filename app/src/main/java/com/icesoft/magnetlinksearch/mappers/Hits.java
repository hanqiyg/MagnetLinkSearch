package com.icesoft.magnetlinksearch.mappers;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Set;

public class Hits {
    @JsonProperty(value = "total")
    Total total;
    @JsonProperty(value = "max_score")
    float maxScore;
    @JsonProperty(value = "hits")
    List<InnerHits> innerHits;

    public Total getTotal() {
        return total;
    }

    public void setTotal(Total total) {
        this.total = total;
    }

    public float getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(float maxScore) {
        this.maxScore = maxScore;
    }

    public List<InnerHits> getInnerHits() {
        return innerHits;
    }

    public void setInnerHits(List<InnerHits> innerHits) {
        this.innerHits = innerHits;
    }
}
