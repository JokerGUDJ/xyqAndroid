package com.xej.xhjy.ui.society.bean;

import com.mcxtzhang.indexlib.IndexBar.bean.BaseIndexBean;


/**
 * @ProjectName: XHJYMobileClient
 * @Package: com.xej.xhjy.ui.society.bean
 * @ClassName: BaseIndexPinyinBean
 * @Description: 索引类的汉语拼音的接口
 * @Author: lihy_0203
 * @CreateDate: 2019/7/2 上午9:34
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/7/2 上午9:34
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public abstract class BaseIndexPinyinBean extends BaseIndexBean {
    private String baseIndexPinyin;//城市的拼音

    public String getBaseIndexPinyin() {
        return baseIndexPinyin;
    }

    public BaseIndexPinyinBean setBaseIndexPinyin(String baseIndexPinyin) {
        this.baseIndexPinyin = baseIndexPinyin;
        return this;
    }

    //是否需要被转化成拼音， 类似微信头部那种就不需要 美团的也不需要
    //微信的头部 不需要显示索引
    //美团的头部 索引自定义
    //默认应该是需要的
    public boolean isNeedToPinyin() {
        return true;
    }

    //需要转化成拼音的目标字段
    public abstract String getTarget();


}
