package com.xej.xhjy.ui.login;
/**
 * @class LoginCallBack
 * @author dazhi
 * @Createtime 2018/7/10 09:33
 * @description 校验登录后再跳转的类
 * @Revisetime
 * @Modifier
 */
public abstract class LoginCallBack {
    /**
     * 标记是否可用
     */
    public boolean canUse = true;
    /**
     * 标记是否检查认证用户
     */
    public boolean isPass = false;
    /**
     * 标记是否检查机构用户
     */
    public boolean isOrg = false;
    /**
     * 校验或者登录完成后执行的代码方法
     */
    public abstract void loginAfterRun();
}
