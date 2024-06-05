package com.xej.xhjy.event;

public class VideoAudioCallEvent {

    private int msgType;

    public VideoAudioCallEvent(int msgType) {
        this.msgType = msgType;
    }

    public int getMsgType() {
        return msgType;
    }
}
