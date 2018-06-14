package com.kck4156.uisample.viewdata;

import java.util.List;

public class ChatLogs {
    private long epochTime;
    private List<ChatLog> logs;
    public  ChatLogs() {}
    public  ChatLogs(long epochTime, List<ChatLog> logs) {
        this.epochTime = epochTime;
        this.logs = logs;
    }

    public long getEpochTime() {
        return epochTime;
    }

    public void setEpochTime(long epochTime) {
        this.epochTime = epochTime;
    }

    public List<ChatLog> getLogs() {
        return logs;
    }

    public void setLogs(List<ChatLog> logs) {
        this.logs = logs;
    }
}
