package com.xej.xhjy.ui.main;

/**
 * @author dazhi
 * @class LoginEvent
 * @Createtime 2018/7/9 09:46
 * @description 收到jpush进行全局通知
 * @Revisetime
 * @Modifier
 */
public class RecivedJpusheEvent {
    private boolean isHas;

    public RecivedJpusheEvent(boolean isHas) {
        this.isHas = isHas;
    }

    public boolean getHasMessage() {
        return isHas;
    }
}
