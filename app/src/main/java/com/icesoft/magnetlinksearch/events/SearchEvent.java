package com.icesoft.magnetlinksearch.events;

import java.util.Objects;

public class SearchEvent {
    public enum Status{
        FIRST_SUCCESS,FIRST_ERROR,FIRST_EMPTY,MORE_SUCCESS,MORE_ERROR,MORE_EMPTY;
    }
    private Status status;
    private int from;
    private int size;
    private String keyword;
    private String message;

    public SearchEvent(Status status, int from, int size, String keyword, String message) {
        this.status = status;
        this.from = from;
        this.size = size;
        this.keyword = keyword;
        this.message = message;
    }

    @Override
    public String toString() {
        return "SearchEvent{" +
                "status=" + status +
                ", from=" + from +
                ", size=" + size +
                ", keyword='" + keyword + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SearchEvent that = (SearchEvent) o;
        return from == that.from &&
                size == that.size &&
                status == that.status &&
                Objects.equals(keyword, that.keyword) &&
                Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, from, size, keyword, message);
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
