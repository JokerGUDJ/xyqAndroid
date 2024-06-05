package com.xej.xhjy.tools;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import com.netease.nim.uikit.api.NimUIKit;
import com.xej.xhjy.R;
import com.xej.xhjy.common.base.BaseActivity;
import com.xej.xhjy.common.storage.PerferenceUtils;
import com.xej.xhjy.common.utils.AppConstants;
import com.xej.xhjy.common.utils.MD5Utils;
import com.xej.xhjy.https.NetConstants;
import com.xej.xhjy.common.view.Dialog.ClubSingleBtnDialog;
import com.xej.xhjy.common.view.Dialog.PositiveListener;
import com.xej.xhjy.ui.login.LoginActivity;
import com.xej.xhjy.ui.login.LoginCallBack;
import com.xej.xhjy.ui.login.LoginOutEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * @author dazhi
 * @class LoginUtils
 * @Createtime 2018/7/9 16:15
 * @description 跳转登录验证，登出等操作
 * @Revisetime
 * @Modifier
 */
public class LoginUtils {

    /**
     * 检查是否登录的跳转
     *
     * @param activity
     * @param callBack
     */
    public static void startCheckLogin(BaseActivity activity, LoginCallBack callBack) {
        if (AppConstants.IS_LOGIN) {
            if (callBack.isPass) {
                if ("N".equals(AppConstants.USER_STATE)) { //必须认证
                    if (callBack.isOrg) {
                        if (isOrgUser()) {
                            callBack.loginAfterRun();
                        } else {
                            showOrgMessage(activity);
                        }
                    } else {
                        callBack.loginAfterRun();
                    }
                } else {
                    showCerMessage(activity);
                }
            } else {
                callBack.loginAfterRun();
            }
            //已经登录标记为不可用
            callBack.canUse = false;
        } else {
            activity.startActivity(new Intent(activity, LoginActivity.class));
        }
    }

    /**
     * 未认证的提示
     */
    public static void showCerMessage(BaseActivity activity) {
        ClubSingleBtnDialog dialog = new ClubSingleBtnDialog(activity);
        dialog.setMessage("为了您的权益，请您耐心等待认证审核！");
        dialog.show();
    }

    /**
     * 获取是否是机构用户
     *
     * @return
     */
    public static boolean isOrgUser() {
        if (AppConstants.USER_ROLES.contains("org_manager")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 非机构用户的提示
     */
    public static void showOrgMessage(BaseActivity activity) {
        ClubSingleBtnDialog dialog = new ClubSingleBtnDialog(activity);
        dialog.setMessage("个人用户无法报名会议，请联系您的机构处理！");
        dialog.show();
    }


    /**
     * 获取账户认证信息
     *
     * @return
     */
    public static Drawable getUserCerInfo(Context context) {
        Drawable cer = null;
        if ("W".equals(AppConstants.USER_STATE)) {
            cer = context.getDrawable(R.drawable.w_label);
        } else if ("R".equals(AppConstants.USER_STATE)) {
            cer = context.getDrawable(R.drawable.r_label);
        } else if ("N".equals(AppConstants.USER_STATE)) {
            cer = context.getDrawable(R.drawable.n_label);
        }
        return cer;
    }

    /**
     * 获取会议状态String
     *
     * @param stt 会议状态码
     * @return
     */
    public static String getMeetState(String stt) {
        if (AppConstants.DATA_DICTIONARY != null) {
            return AppConstants.DATA_DICTIONARY.optJSONObject("stt").optString(stt);
        } else {
            if (stt.equals("00")) {
                return "报名中";
            } else if (stt.equals("10")) {
                return "进行中";
            } else if (stt.equals("20")) {
                return "已结束";
            } else if (stt.equals("30")) {
                return "报名结束";
            } else {
                return "进行中";
            }
        }
    }

    /**
     * 登出清除信息
     */
    public static void clearLoginInfo() {
        //清除用户信息
        AppConstants.IS_LOGIN = false;
        AppConstants.USER_ID = "";
        PerferenceUtils.removeKey(AppConstants.User.NAME);
        PerferenceUtils.removeKey(AppConstants.User.TEL);
        PerferenceUtils.removeKey(AppConstants.User.ID);
        PerferenceUtils.removeKey(AppConstants.User.COMPLANY);
        PerferenceUtils.removeKey(AppConstants.User.DEPARTMENT);
        PerferenceUtils.removeKey(AppConstants.User.EMAIL);
        PerferenceUtils.removeKey(AppConstants.User.ADDRESS);
        PerferenceUtils.removeKey(AppConstants.User.JOB);
        PerferenceUtils.removeKey(AppConstants.User.ROLES);
        PerferenceUtils.removeKey(AppConstants.User.STATE);
        PerferenceUtils.removeKey(AppConstants.User.IM_CHAT_ACCOUNT);
        PerferenceUtils.removeKey(AppConstants.User.IM_CHAT_TOKEN);
        PerferenceUtils.removeKey(AppConstants.DATA_TRAINING_ID_KEY);
        NimUIKit.setAccount(null);

        //清除网络请求的信息
        String cookieKey = PerferenceUtils.get(NetConstants.COOKIE_KEY, "");
        if (!TextUtils.isEmpty(cookieKey)) {
            String[] keys = cookieKey.split(",");
            for (String key : keys) {
                PerferenceUtils.removeKey(key);
            }
        }
        PerferenceUtils.removeKey(NetConstants.COOKIE_KEY);
        EventBus.getDefault().post(new LoginOutEvent("用户登出！"));
    }


    /**
     * 获取头像地址，上传时以用户ID为地址，故这里只需要用用户ID拼装头像地址
     *
     * @return
     */
    public static String getHeadImagUrl() {
        String filename = PerferenceUtils.get(AppConstants.User.ID, "empty") + ".jpg";
        return NetConstants.HEAD_IMAG_URL + filename;
    }

    public static void check(final BaseActivity activity) {
        if (AppConstants.IS_DEBUG) {
            return;
        }
//        Log.d("NetworkTypeUtils", "4589g" + GenalralUtils.getInfo(activity) + "84649tjidmbk0099bkd009");
//        String info = activity.getString(R.string.my_check).split("ab")[1];
//        if (!info.equals(GenalralUtils.getInfo(activity)+"")) {
//            ClubSingleBtnDialog dialog = new ClubSingleBtnDialog(activity);
//            dialog.setMessage("当前应用已被篡改！请下载官方最新版本！");
//            dialog.setPositiveListener("确定", new PositiveListener() {
//                @Override
//                public void onPositiveClick() {
//                    activity.finish();
//                }
//            });
//            dialog.show();
//        } else {
            String info2 = activity.getString(R.string.my_check2).split("cdd")[1];
            if (!info2.equals(MD5Utils.getSignMd5Str(activity))) {
                ClubSingleBtnDialog dialog = new ClubSingleBtnDialog(activity);
                dialog.setMessage("当前应用签名不一致！请下载官方最新版本！");
                dialog.setPositiveListener("确定", new PositiveListener() {
                    @Override
                    public void onPositiveClick() {
                        activity.finish();
                    }
                });
                dialog.show();
            }
//        }
    }
}
