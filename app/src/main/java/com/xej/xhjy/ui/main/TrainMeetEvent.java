package com.xej.xhjy.ui.main;

/**
 * @ProjectName: XHJY_NEW_UAT
 * @Package: com.xej.xhjy.ui.main
 * @ClassName: TrainMeetEvent
 * @Description: 在我的页面是否显示年度会议以及培训报名,true显示 false 不显示
 * @Author: lihy_0203
 * @CreateDate: 2019/3/15 上午9:55
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/3/15 上午9:55
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class TrainMeetEvent {
    private boolean message;

    public TrainMeetEvent(boolean message) {
        this.message = message;
    }

    public boolean getMessage() {
        return message;
    }
}
