package com.xej.xhjy.receiver;

/**
 * @author dazhi
 * @class LoginEvent
 * @Createtime 2018/7/9 09:46
 * @description 监听网络发送的message
 * @Revisetime
 * @Modifier
 */
public class NetWorkStatusEvent {
    private String message;

    public NetWorkStatusEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
