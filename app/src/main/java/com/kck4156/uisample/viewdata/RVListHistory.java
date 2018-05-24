package com.kck4156.uisample.viewdata;

import java.util.List;

public class RVListHistory {
    private String title;
    private List<ChatLog> log;

    public RVListHistory(String title, List<ChatLog> log) {
        this.title = title;
        this.log = log;
    }

    public String getTitle() {
        return title;
    }

    public List<ChatLog> getLog() {
        return log;
    }
}
