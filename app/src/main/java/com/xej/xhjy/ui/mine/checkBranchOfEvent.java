package com.xej.xhjy.ui.mine;

/**
 * @author lihy_0203
 * @class checkBranchOfEvent
 * @Createtime 2019/3/4 09:46
 * @description 查询是否有专委会，没有去提示维护专委会
 * @Revisetime
 * @Modifier
 */
public class checkBranchOfEvent {
    private String message;

    public checkBranchOfEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
