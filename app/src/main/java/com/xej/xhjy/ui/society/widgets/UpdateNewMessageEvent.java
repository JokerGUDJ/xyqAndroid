package com.xej.xhjy.ui.society.widgets;

/**
 * @author lihy_0203
 * @class BranchOfEditEvent
 * @Createtime 2019/3/4 09:46
 * @description 专委会编辑通知
 * @Revisetime
 * @Modifier
 */
public class UpdateNewMessageEvent {
    private String message;

    public UpdateNewMessageEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
