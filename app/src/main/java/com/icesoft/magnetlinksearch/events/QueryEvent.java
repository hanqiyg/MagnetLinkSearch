package com.icesoft.magnetlinksearch.events;

public class QueryEvent {
    public String query;

    public QueryEvent(String query) {
        this.query = query;
    }

    @Override
    public String toString() {
        return "QueryEvent{" +
                "query='" + query + '\'' +
                '}';
    }
}
