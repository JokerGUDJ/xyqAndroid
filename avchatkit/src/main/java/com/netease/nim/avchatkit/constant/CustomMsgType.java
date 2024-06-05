package com.netease.nim.avchatkit.constant;

public enum CustomMsgType {

    //音视频会议邀请
    MSG_INVITE(0),
    //音视频会议结束
    MSG_HANDUP(1),
    //开始屏幕共享
    MSG_START_SHARE_SCREEM(2),
    //结束屏幕共享
    MSG_END_SHARE_SCREEM(3),
    //邀请人员
    MSG_ADDUSER(4),
    //单独传参通知
    MSG_PARAMETER(5);

    private int value;

    CustomMsgType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
