package com.xej.xhjy.tools;


import android.text.TextUtils;

/**
 * @ProjectName: PWD-MobileClient
 * @Package: com.xej.xhjy.tools
 * @ClassName: NormalUtils
 * @Description: 普通的工具类
 * @Author: lihy_0203
 * @CreateDate: 2020/1/7 上午11:15
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/1/7 上午11:15
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class NormalUtils {
    /**
     * 手机号码脱敏
     * @param mobile
     * @return
     */
    public static String mobileEncrypt(String mobile) {
        if (TextUtils.isEmpty(mobile) || (mobile.length() != 11)) {
            return mobile;
        }
        return mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

    /**
     * 身份证号码脱敏
     * @param id
     * @return
     */
    public static String idEncrypt(String id){
        if(TextUtils.isEmpty(id) || (id.length() < 8)){
            return id;
        }
        return id.replaceAll("(?<=\\w{3})\\w(?=\\w{4})", "*");
    }


}
