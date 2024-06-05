package com.xej.xhjy.ui.society.bean;

import com.mcxtzhang.indexlib.suspension.ISuspensionInterface;


/**
 * @ProjectName: XHJYMobileClient
 * @Package: com.xej.xhjy.ui.society.bean
 * @ClassName: BaseIndexBean
 * @Description: 索引类的标志位的实体基类
 * @Author: lihy_0203
 * @CreateDate: 2019/7/2 上午9:34
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/7/2 上午9:34
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public abstract class BaseIndexBean implements ISuspensionInterface {
    private String baseIndexTag;//所属的分类（汉语拼音首字母）

    public String getBaseIndexTag() {
        return baseIndexTag;
    }

    public BaseIndexBean setBaseIndexTag(String baseIndexTag) {
        this.baseIndexTag = baseIndexTag;
        return this;
    }

    @Override
    public String getSuspensionTag() {
        return baseIndexTag;
    }

    @Override
    public boolean isShowSuspension() {
        return true;
    }
}
