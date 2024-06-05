package com.xej.xhjy.ui.login;

/**
 * @author dazhi
 * @class LoginOutEvent
 * @Createtime 2018/7/9 09:46
 * @description 登出成功发送的message
 * @Revisetime
 * @Modifier
 */
public class LoginOutEvent {
    private String message;

    public LoginOutEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
