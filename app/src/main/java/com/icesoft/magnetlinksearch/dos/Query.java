package com.icesoft.magnetlinksearch.dos;

import java.util.Objects;

public class Query {
    private Type type;
    private String queryString;
    private int from;
    private int size;

    public enum Type{
        StringQuery,IdQuery
    }

    public Query() {
    }

    @Override
    public String toString() {
        return "Query{" +
                "type=" + type +
                ", queryString='" + queryString + '\'' +
                ", from=" + from +
                ", size=" + size +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Query query = (Query) o;
        return from == query.from &&
                size == query.size &&
                type == query.type &&
                Objects.equals(queryString, query.queryString);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, queryString, from, size);
    }

    public Query(Type type, String queryString, int from, int size) {
        this.type = type;
        this.queryString = queryString;
        this.from = from;
        this.size = size;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
