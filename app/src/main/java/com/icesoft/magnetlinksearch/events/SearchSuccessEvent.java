package com.icesoft.magnetlinksearch.events;

import com.icesoft.magnetlinksearch.models.Magnet;

import java.util.List;

public class SearchSuccessEvent {
    public boolean refresh;
    public int total;
    public List<Magnet> magnets;

    public SearchSuccessEvent(boolean refresh,int total,List<Magnet> magnets){
        this.refresh = refresh;
        this.total = total;
        this.magnets = magnets;
    }
}
