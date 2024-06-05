package com.xej.xhjy.ui.login;

/**
 * @author dazhi
 * @class LoginEvent
 * @Createtime 2018/7/9 09:46
 * @description 登录成功发送的message
 * @Revisetime
 * @Modifier
 */
public class LoginEvent {
    private String message;

    public LoginEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
