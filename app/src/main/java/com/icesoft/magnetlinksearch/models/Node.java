package com.icesoft.magnetlinksearch.models;

public class Node {
    public String name;
    public long size;

    public Node(String name, long size) {
        this.name = name;
        this.size = size;
    }

    @Override
    public String toString() {
        return "Node{" +
                "name='" + name + '\'' +
                ", size=" + size +
                '}';
    }
}
