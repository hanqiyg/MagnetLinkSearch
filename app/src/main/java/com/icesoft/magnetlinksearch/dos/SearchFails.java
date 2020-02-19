package com.icesoft.magnetlinksearch.dos;

import java.util.Objects;

public class SearchFails {
    private int statusCode;
    private String message;

    public SearchFails() {
    }

    public SearchFails(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SearchFails that = (SearchFails) o;
        return statusCode == that.statusCode &&
                Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(statusCode, message);
    }

    @Override
    public String toString() {
        return "SearchFails{" +
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
