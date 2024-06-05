package com.xej.xhjy.ui.mine;

/**
 * @author dazhi
 * @class HeadEditEvent
 * @Createtime 2018/7/9 09:46
 * @description 头像修改成功发送的event
 * @Revisetime
 * @Modifier
 */
public class HeadEditEvent {
    private String message;

    public HeadEditEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
