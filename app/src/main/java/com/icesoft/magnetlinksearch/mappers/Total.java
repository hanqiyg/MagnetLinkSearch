package com.icesoft.magnetlinksearch.mappers;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Total {
    @JsonProperty(value = "value")
    int value;
    @JsonProperty(value = "relation")
    String relation;

    public long getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }
}
