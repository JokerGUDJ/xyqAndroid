package com.xej.xhjy.tools;

import android.content.Context;

/**
 * @ProjectName: XHJY_NEW_UAT
 * @Package: com.xej.xhjy.ui.main
 * @ClassName: Singleton
 * @Description: 内存泄漏测试类，此类会导致内存泄漏。
 * @Author: lihy_0203
 * @CreateDate: 2019/3/20 上午9:28
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/3/20 上午9:28
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class Singleton {
    private static Singleton singleton;
    private Context context;

    private Singleton(Context context) {
        this.context = context;
    }

    public static Singleton newInstance(Context context) {
        if (singleton == null) {
            synchronized (Singleton.class) {
                if (singleton == null) {//双重检查锁定
                    singleton = new Singleton(context);
                }
            }
        }
        return singleton;
    }

}
