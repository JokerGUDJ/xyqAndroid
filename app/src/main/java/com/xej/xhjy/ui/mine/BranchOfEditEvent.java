package com.xej.xhjy.ui.mine;

/**
 * @author lihy_0203
 * @class BranchOfEditEvent
 * @Createtime 2019/3/4 09:46
 * @description 专委会编辑通知
 * @Revisetime
 * @Modifier
 */
public class BranchOfEditEvent {
    private String message;

    public BranchOfEditEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
