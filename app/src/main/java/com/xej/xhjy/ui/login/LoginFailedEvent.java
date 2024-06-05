package com.xej.xhjy.ui.login;

/**
 * @class LoginFailedEvent
 * @author dazhi
 * @Createtime 2018/7/10 11:28
 * @description 登录失败全局通知，有些页面跳转需要设置一个登录成功的回调，
 *              当登录失败或者取消时，这个回调仍然存在，当其他页面登录后全局发全局通知
 *              会导致这个回调仍然会执行，故这里增加一个失败的通知以置空所有的回调
 * @Revisetime
 * @Modifier
 */
public class LoginFailedEvent {
    private String message;

    public LoginFailedEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
