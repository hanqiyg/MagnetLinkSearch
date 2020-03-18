package com.icesoft.magnetlinksearch.events;

import com.icesoft.magnetlinksearch.mappers.MFile;

import java.util.List;

public class FileTreeEvent {
    public List<MFile> files;
    public FileTreeEvent(List<MFile> files) {
        this.files = files;
    }
}
