package com.xej.xhjy.ui.login.cropper;


import android.app.Activity;

import com.xej.xhjy.common.base.BaseActivity;
import com.xej.xhjy.common.utils.AppConstants;
import com.xej.xhjy.tools.LoginUtils;
import com.xej.xhjy.ui.login.LoginActivity;

/**
 * @ProjectName: XHJY_NEW_UAT
 * @Package: com.xej.xhjy.ui.login.cropper
 * @ClassName: LoginUtils
 * @Description: 先判断登录，在去进入页面。
 * @Author: lihy_0203
 * @CreateDate: 2019/2/26 下午3:05
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/2/26 下午3:05
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class LoginCheckUtils {

    /**
     * 标记是否检查认证用户
     */
    public static boolean isCertification = false;
    /**
     * 标记是否检查机构用户
     */
    public static boolean isOrganization = false;


    /**
     * 需要判断登录还需要添加认证
     * 或者是否为机构用户等附加条件
     */
    public static void isLogin() {
        //先判断登录
        if (AppConstants.IS_LOGIN) {
            //登录了就执行
            if (iLogin != null) {
                if (isCertification) {
                    //必须认证
                    if ("N".equals(AppConstants.USER_STATE)) {
                        if (isOrganization) {
                            if (LoginUtils.isOrgUser()) {
                                LoginCheckUtils.iLogin.onlogin();
                            } else {
                                LoginUtils.showOrgMessage((BaseActivity) activity);
                            }
                        } else {
                            LoginCheckUtils.iLogin.onlogin();
                        }
                    } else {
                        LoginUtils.showCerMessage((BaseActivity) activity);
                    }
                }

                //登录后清除上下文
                clear();
            }
        } else {
            //去登录界面
            LoginActivity.start(activity);
        }
    }

    /**
     * 只需要登录权限即可进入
     */
    public static void OnlyCheckLogin(){
        if (AppConstants.IS_LOGIN) {

            LoginCheckUtils.iLogin.onlogin();

        }else{
            LoginActivity.start(activity);
        }

    }

    public static void clear() {
        if (iLogin != null) {
            iLogin = null;
        }
        if (activity != null) {
            activity = null;
        }
        //校验后需要把状态置为false
        isCertification = false;
        isOrganization = false;

    }

    public static ILogin iLogin;
    public static Activity activity;

    public static void setIlogin(ILogin ilogin, Activity activity) {
        LoginCheckUtils.iLogin = ilogin;
        LoginCheckUtils.activity = activity;
        isLogin();
    }

    public interface ILogin {
        void onlogin();
    }

}
