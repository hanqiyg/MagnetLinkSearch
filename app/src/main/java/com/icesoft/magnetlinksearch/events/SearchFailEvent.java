package com.icesoft.magnetlinksearch.events;

public class SearchFailEvent {
    private int statusCode;
    private String message;

    public SearchFailEvent() {
    }

    public SearchFailEvent(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
    @Override
    public String toString() {
        return "SearchFailEvent{" +
                "statusCode=" + statusCode +
                ", message='" + message + '\'' +
                '}';
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
