package com.icesoft.magnetlinksearch.events;

public class NetworkChangeEvent {
    public boolean isConnected;
    public int networkType;
    public NetworkChangeEvent(boolean isConnected, int networkType) {
        this.isConnected = isConnected;
        this.networkType = networkType;
    }
}
