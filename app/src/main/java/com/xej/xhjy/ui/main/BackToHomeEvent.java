package com.xej.xhjy.ui.main;

/**
 * @class BackToHomeEvent
 * @author dazhi
 * @Createtime 2018/7/10 11:28
 * @description 返回首页的消息
 * @Revisetime
 * @Modifier
 */
public class BackToHomeEvent {
    private String message;

    public BackToHomeEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
