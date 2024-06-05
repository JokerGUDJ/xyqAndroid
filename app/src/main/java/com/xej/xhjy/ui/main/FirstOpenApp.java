package com.xej.xhjy.ui.main;

import android.content.Context;
import android.view.Gravity;

import com.xej.xhjy.common.storage.PerferenceUtils;
import com.xej.xhjy.common.utils.AppConstants;
import com.xej.xhjy.ui.dialog.ClubTextHighlightDialog;
import com.xej.xhjy.common.view.Dialog.NegativeListener;
import com.xej.xhjy.common.view.Dialog.PositiveListener;


/**
 * @ProjectName: XHJYMobileClient
 * @Package: com.xej.xhjy.ui.main
 * @ClassName: FirstOpenApp
 * @Description: 第一次打开app
 * @Author: lihy_0203
 * @CreateDate: 2019/12/10 下午4:42
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/12/10 下午4:42
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class FirstOpenApp {

    public static void isFirstOpen(Context context) {
        ClubTextHighlightDialog dialog = new ClubTextHighlightDialog(context);
        dialog.setTitle("温馨提示");
        dialog.setMessageGravity(Gravity.CENTER_VERTICAL);
        dialog.setPositiveListener("同意", new PositiveListener() {
            @Override
            public void onPositiveClick() {
                PerferenceUtils.put(AppConstants.IS_FIRST, false);
            }
        });
        dialog.setNegativeListener("取消", new NegativeListener() {
            @Override
            public void onNegativeClick() {
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
            }
        });
        //设置点击屏幕不消失
        dialog.setCanceledOnTouchOutside(false);
        //设置点击返回键不消失
        dialog.setCancelable(false);
        dialog.show();

    }
}
