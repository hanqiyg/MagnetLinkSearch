package com.icesoft.magnetlinksearch.dos;

import java.util.Objects;

public class Response {
    private Code code;
    private String message;
    private Object payload;
    private int from;
    private int size;
    private int total;
    public enum Code{
        QueryList,QuerySingle,ErrorServer,ErrorUnknow;
    }

    public Response() {
    }

    public Response(Code code, String message, Object payload, int from, int size, int total) {
        this.code = code;
        this.message = message;
        this.payload = payload;
        this.from = from;
        this.size = size;
        this.total = total;
    }

    @Override
    public String toString() {
        return "Response{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", payload=" + payload +
                ", from=" + from +
                ", size=" + size +
                ", total=" + total +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Response response = (Response) o;
        return from == response.from &&
                size == response.size &&
                total == response.total &&
                code == response.code &&
                Objects.equals(message, response.message) &&
                Objects.equals(payload, response.payload);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, message, payload, from, size, total);
    }

    public Code getCode() {
        return code;
    }

    public void setCode(Code code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
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

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
