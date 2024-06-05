package com.netease.nim.uikit.business.team.suspension;

/**
 * @ProjectName: XEJClubClient
 * @Package: com.xej.xhjy.ui.society.suspension
 * @ClassName: ISuspensionInterface
 * @Description: 悬停接口
 * @Author: lihy_0203
 * @CreateDate: 2018/12/26 下午5:08
 * @UpdateUser: 更新者
 * @UpdateDate: 2018/12/26 下午5:08
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */

public interface ISuspensionInterface {
    //是否需要显示悬停title
    boolean isShowSuspension();

    //悬停的title
    String getSuspensionTag();

}
