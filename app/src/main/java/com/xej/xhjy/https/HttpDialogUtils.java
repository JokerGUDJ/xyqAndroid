package com.xej.xhjy.https;

import android.content.Intent;

import com.xej.xhjy.common.base.BaseActivity;
import com.xej.xhjy.common.utils.GenalralUtils;
import com.xej.xhjy.common.view.Dialog.PositiveListener;
import com.xej.xhjy.tools.LoginUtils;
import com.xej.xhjy.common.view.Dialog.ClubSingleBtnDialog;
import com.xej.xhjy.ui.login.LoginActivity;

import org.json.JSONObject;

/**
 * @author dazhi
 * @class HttpDialogUtils
 * @Createtime 2018/8/10 09:21
 * @description describe 网络异常统一弹窗管理类
 * @Revisetime
 * @Modifier
 */
public class HttpDialogUtils {

    public static void doBackString(final BaseActivity activity, String httpBackString) {
        try {
            JSONObject jsonObject = new JSONObject(httpBackString);
            String codeString = jsonObject.optString("code");
            int code = Integer.parseInt(codeString);
            if (code == 0) {
                return;
            }
            if (code == 1007) {//权限不够
                PositiveListener listener = new PositiveListener() {
                    @Override
                    public void onPositiveClick() {
                        positiveClick(activity);
                    }
                };
                doHttpDialog(activity, "您已手动退出或在其它设备上登录，请重新登录", listener);
            } else if (code == 1008) {//交易不对外公开
                PositiveListener listener = new PositiveListener() {
                    @Override
                    public void onPositiveClick() {
                        positiveClick(activity);
                    }
                };
                doHttpDialog(activity, "您已手动退出或在其它设备上登录，请重新登录", listener);
            } else if (code == 1009 || code ==4005) {//token为空 token不存在
                PositiveListener listener = new PositiveListener() {
                    @Override
                    public void onPositiveClick() {
                        positiveClick(activity);;
                    }
                };
                doHttpDialog(activity, "您已手动退出或在其它设备上登录，请重新登录", listener);
            } else if (code == 1010) {//密匙为空
                PositiveListener listener = new PositiveListener() {
                    @Override
                    public void onPositiveClick() {
                        positiveClick(activity);
                    }
                };
                doHttpDialog(activity, "您已手动退出或在其它设备上登录，请重新登录", listener);
            } else if (code == 1011) {//获取密匙失败
                PositiveListener listener = new PositiveListener() {
                    @Override
                    public void onPositiveClick() {
                        positiveClick(activity);
                    }
                };
                doHttpDialog(activity, "您已手动退出或在其它设备上登录，请重新登录", listener);
            } else if (code == 1012) {//验证失败
                PositiveListener listener = new PositiveListener() {
                    @Override
                    public void onPositiveClick() {
                        positiveClick(activity);
                    }
                };
                doHttpDialog(activity, "您已手动退出或在其它设备上登录，请重新登录", listener);
            } else if (code == 1013) {//获取tokenbody失败
                PositiveListener listener = new PositiveListener() {
                    @Override
                    public void onPositiveClick() {
                        positiveClick(activity);
                    }
                };
                doHttpDialog(activity, "您已手动退出或在其它设备上登录，请重新登录", listener);
            } else if (code == 1014) { //交易需要管理员权限
                PositiveListener listener = new PositiveListener() {
                    @Override
                    public void onPositiveClick() {
                        positiveClick(activity);
                    }
                };
                doHttpDialog(activity, "您已手动退出或在其它设备上登录，请重新登录", listener);
            }else if(code == 4005){
                PositiveListener listener = new PositiveListener() {
                    @Override
                    public void onPositiveClick() {
                        positiveClick(activity);
                    }
                };
                doHttpDialog(activity, "未登录，请重新登录", listener);

            }else {
                if (code != 1202 && code != 4005 && code != 4010 && code != 4007 && code != 3068){ //4005未登录 4010已签到 4007暂未报名, 3068报名时非被机构用户
                    String mess = jsonObject.optString("msg");
                    if (GenalralUtils.isEmpty(mess)) {
                        mess = "服务器错误";
                    }
                    PositiveListener listener = new PositiveListener() {
                        @Override
                        public void onPositiveClick() {
                            if(activity.dialog != null){
                                activity.dialog.dismiss();
                                activity.dialog = null;
                            }
                        }
                    };
                    doHttpDialog(activity, mess, listener);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void positiveClick(BaseActivity activity){
        LoginUtils.clearLoginInfo();
        if(activity.dialog != null){
            activity.dialog.dismiss();
            activity.dialog = null;
        }
        activity.startActivityWithAnim(new Intent(activity, LoginActivity.class));
    }

    public static void doHttpDialog(BaseActivity activity, String message, PositiveListener listener) {
        if(activity.dialog == null){
            activity.dialog = new ClubSingleBtnDialog(activity);
        }
        activity.dialog.setTitle("温馨提示");
        activity.dialog.setMessage(message);
        if (listener != null) {
            activity.dialog.setPositiveListener("确定", listener);
        }
        if(!activity.dialog.isShowing()){
            activity.dialog.show();
        }else{
            activity.dialog.update();
        }
    }
}
