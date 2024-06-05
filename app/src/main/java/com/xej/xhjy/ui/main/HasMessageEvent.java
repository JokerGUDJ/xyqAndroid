package com.xej.xhjy.ui.main;

/**
 * @author dazhi
 * @class LoginEvent
 * @Createtime 2018/7/9 09:46
 * @description 查询是否有新消息
 * @Revisetime
 * @Modifier
 */
public class HasMessageEvent {
    private boolean isHas;

    public HasMessageEvent(boolean isHas) {
        this.isHas = isHas;
    }

    public boolean getHasMessage() {
        return isHas;
    }
}
