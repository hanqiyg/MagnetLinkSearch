package com.icesoft.magnetlinksearch.mappers;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InnerHits {
    @JsonProperty(value = "_index")
    String index;
    @JsonProperty(value = "_type")
    String type;
    @JsonProperty(value = "_id")
    String id;
    @JsonProperty(value = "_score")
    float score;
    @JsonProperty(value = "_source")
    Source source;

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }
}
